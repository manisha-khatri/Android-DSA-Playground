package com.example.systemdesign.prep.paypal

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricManager
import androidx.core.content.ContextCompat
import com.example.systemdesign.R
import android.provider.Settings

class BiometricActivity : AppCompatActivity() {
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric)

        // Executor and callback: runs on main thread
        val executor = ContextCompat.getMainExecutor(this)

        // Build a BiometricPrompt with an Executor + AuthenticationCallback.
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@BiometricActivity, "Auth error: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                // handle success (unlock UI / fetch secret / navigate)
                Toast.makeText(this@BiometricActivity, "Authenticated ✅", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@BiometricActivity, "Authentication failed — try again", Toast.LENGTH_SHORT).show()
            }
        })

        // 2) Prompt info: title and negative action
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Confirm your identity")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use password") // required unless using device-credential fallback
            .build()

        val btn = findViewById<Button>(R.id.btn_auth)
        btn.setOnClickListener {
            val biometricManager = BiometricManager.from(this)

            // Check device capability with BiometricManager.canAuthenticate(...).
            //BiometricManager.from(this).canAuthenticate(...) checks hardware + enrollment; use constants from BiometricManager.Authenticators. If not enrolled, we start an enroll Intent.
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    //Call biometricPrompt.authenticate(promptInfo) when the button is tapped.
                    //(If user isn’t enrolled, send them to enroll in settings.)
                    biometricPrompt.authenticate(promptInfo)
                }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    // Send user to settings to enroll biometrics
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                            putExtra(
                                Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                BiometricManager.Authenticators.BIOMETRIC_STRONG
                            )
                        }
                        startActivity(enrollIntent)
                    } else {
                        // Fallback for older versions
                        startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
                    }
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    Toast.makeText(this, "Biometric sensor not available", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
