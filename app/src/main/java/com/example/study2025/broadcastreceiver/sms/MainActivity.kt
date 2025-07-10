package com.example.study2025.broadcastreceiver.sms

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.study2025.R

class MainActivity3 : AppCompatActivity() {

    private lateinit var smsContentTextView: TextView
    private lateinit var smsDisplayReceiver: SmsDisplayReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_broadcast_receiver_battery)

        smsContentTextView = findViewById(R.id.smsContentTextView)
        smsContentTextView.text = getString(R.string.waiting_for_sms)

        requestSmsPermission()

        smsDisplayReceiver = SmsDisplayReceiver()
        val filter = IntentFilter(SmsReceiver.ACTION_SMS_RECEIVED)
        LocalBroadcastManager.getInstance(this).registerReceiver(smsDisplayReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(smsDisplayReceiver)
    }

    private fun requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECEIVE_SMS),
                PERMISSION_REQUEST_RECEIVE_SMS
            )
        } else {
            showToast("RECEIVE_SMS permission already granted.")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_RECEIVE_SMS) {
            val message = if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                "RECEIVE_SMS permission granted!"
            } else {
                "RECEIVE_SMS permission denied. Cannot receive SMS."
            }
            showToast(message)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Receives local broadcasts (inside the app only) to update the UI (like showing the received SMS in the TextView).
     *
     * SmsDisplayReceiver (inner class):
     * - Registered dynamically in the MainActivity (LocalBroadcastManager).
     * - Only lives while the Activity is alive.
     * - Safe to update UI directly (TextView) because it's tied to the Activity’s lifecycle.
     */
    inner class SmsDisplayReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == SmsReceiver.ACTION_SMS_RECEIVED) {
                val sender = intent.getStringExtra(SmsReceiver.EXTRA_SMS_SENDER)
                val body = intent.getStringExtra(SmsReceiver.EXTRA_SMS_BODY)

                val displayMessage = """
                    Last SMS Received:
                    From: $sender
                    Message: $body

                    Waiting for next SMS...
                """.trimIndent()

                smsContentTextView.text = displayMessage
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_RECEIVE_SMS = 1
    }
}