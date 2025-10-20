package com.example.systemdesign.prep.paypal

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.biometric.BiometricPrompt // ✅ Correct Import
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

class BiometricComposeApp : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    Scaffold{ innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            BiometricButton()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun BiometricButton() {
    val context = LocalContext.current
    val activity = context as FragmentActivity
    val executor = ContextCompat.getMainExecutor(activity)

    // ✅ Callback setup
    val callback = remember {
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(activity, "Authenticated ✅", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(activity, "Error: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(activity, "Authentication failed ❌", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ✅ Initialize BiometricPrompt
    val biometricPrompt = remember {
        BiometricPrompt(activity, executor, callback)
    }

    // ✅ Info dialog
    val promptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Confirm your identity")
            .setSubtitle("Use biometrics to continue")
            .setNegativeButtonText("Cancel")
            .build()
    }

    // ✅ Compose UI
    Button(onClick = { biometricPrompt.authenticate(promptInfo) }) {
        Text(text = "Authenticate with Biometrics")
    }
}