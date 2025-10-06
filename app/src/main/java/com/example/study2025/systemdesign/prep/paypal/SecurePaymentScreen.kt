package com.example.study2025.systemdesign.prep.paypal

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

/**
 * “Design a secure payment screen with biometric authentication.”
 *
 * Key points:
 * - Using Face ID / Touch ID (BiometricPrompt)
 * - Secure storage with EncryptedSharedPreferences
 * - MVVM architecture + Hilt dependency injection
 * - Balancing UX & security
 */

// ---------------- Secure Storage ----------------
// Store the Payment Token Securely
// This saves the user’s payment session token — something like tx_1696420000321.
class SecureStorage(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun putToken(token: String) {
        prefs.edit().putString("payment_token", token).apply()
    }

    fun getToken(): String? = prefs.getString("payment_token", null)

    fun clearToken() {
        prefs.edit().remove("payment_token").apply()
    }
}

// ---------------- Repository ----------------
class PaymentRepository @Inject constructor(
    private val storage: SecureStorage
) {
    suspend fun createPayment(request: PaymentRequest): Result<String> {
        delay(1000) // simulate network delay
        return try {
            val txToken = "tx_" + System.currentTimeMillis()
            storage.putToken(txToken)
            Result.success(txToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getSavedToken(): String? = storage.getToken()
    fun clearPaymentToken() = storage.clearToken()
}

// ---------------- ViewState ----------------
sealed class PaymentViewState {
    object Idle : PaymentViewState()
    object Authenticating : PaymentViewState()
    object Processing : PaymentViewState()
    data class Success(val txToken: String) : PaymentViewState()
    data class Error(val message: String) : PaymentViewState()
}

// ---------------- Model ----------------
data class PaymentRequest(
    val amount: Double,
    val currency: String,
    val recipient: String
)

// ---------------- ViewModel ----------------
@HiltViewModel
class PaymentViewModel @Inject constructor(private val repo: PaymentRepository) : ViewModel() {
    private val _state = MutableStateFlow<PaymentViewState>(PaymentViewState.Idle)
    val state: StateFlow<PaymentViewState> = _state

    fun onStartAuth() { _state.value = PaymentViewState.Authenticating }

    fun onAuthSuccess(request: PaymentRequest) {
        _state.value = PaymentViewState.Processing
        viewModelScope.launch {
            try {
                val res = repo.createPayment(request)
                res.fold(
                    onSuccess = { _state.value = PaymentViewState.Success(it) },
                    onFailure = { _state.value = PaymentViewState.Error(it.message ?: "Unknown error") }
                )
            } catch (e: Exception) {
                _state.value = PaymentViewState.Error(e.message ?: "Unexpected error")
            }
        }
    }

    fun onAuthError(msg: String) { _state.value = PaymentViewState.Error(msg) }
}

// ---------------- Biometric Helper ----------------
class BiometricHelper(private val activity: FragmentActivity) {
    private val executor: Executor = ContextCompat.getMainExecutor(activity)

    fun canAuthenticate(): Boolean {
        val manager = BiometricManager.from(activity)
        return manager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_WEAK or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }

    fun promptAuthentication(
        title: String,
        subtitle: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val prompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    onError(errString.toString())
                }

                override fun onAuthenticationFailed() {
                    onError("Authentication failed")
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle ?: "")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_WEAK or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        prompt.authenticate(promptInfo)
    }
}

// ---------------- UI ----------------
@Composable
fun PaymentScreen(
    viewModel: PaymentViewModel = hiltViewModel(),
    biometricHelper: BiometricHelper,
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val onAuthenticate = {
        if (biometricHelper.canAuthenticate()) {
            viewModel.onStartAuth()
            biometricHelper.promptAuthentication(
                title = "Confirm Payment",
                subtitle = "Use biometrics or device PIN",
                onSuccess = {
                    val req = PaymentRequest(amount = 9.99, currency = "USD", recipient = "merchant_123")
                    viewModel.onAuthSuccess(req)
                },
                onError = { msg -> viewModel.onAuthError(msg) }
            )
        } else {
            Toast.makeText(context, "Biometric unavailable.", Toast.LENGTH_SHORT).show()
        }
    }

    PaymentContent(state, onAuthenticate)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentContent(state: PaymentViewState, onAuthenticate: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Secure Payment") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                is PaymentViewState.Idle -> Text("Ready to pay securely.")
                is PaymentViewState.Authenticating -> Text("Waiting for biometric confirmation...")
                is PaymentViewState.Processing -> Text("Processing payment...")
                is PaymentViewState.Success -> Text("✅ Success! Token: ${state.txToken}")
                is PaymentViewState.Error -> Text("❌ Error: ${state.message}")
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onAuthenticate,
                enabled = state !is PaymentViewState.Processing && state !is PaymentViewState.Authenticating
            ) {
                Text("Pay $9.99")
            }

            if (state is PaymentViewState.Processing || state is PaymentViewState.Authenticating) {
                Spacer(Modifier.height(24.dp))
                CircularProgressIndicator()
            }
        }
    }
}

// ---------------- Activity ----------------
@AndroidEntryPoint
class SecurePaymentScreenActivity : FragmentActivity() { // 👈 Changed from ComponentActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val biometricHelper = BiometricHelper(this)

        setContent {
            MaterialTheme {
                PaymentScreen(biometricHelper = biometricHelper)
            }
        }
    }
}

// ---------------- Hilt Module ----------------
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideSecureStorage(@ApplicationContext context: Context): SecureStorage = SecureStorage(context)
}

@HiltAndroidApp
class SecurePaymentScreenApp: Application()
