package com.example.study2025.broadcastreceiver.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager

/**
 * Receives system-level broadcasts (from the Android OS) for actual SMS_RECEIVED events.
 *
 * SmsReceiver:
 * - Registered in the Manifest.
 * - Listens to system-wide broadcast: "android.provider.Telephony.SMS_RECEIVED".
 * - This receiver cannot update the UI directly because it’s triggered in the background (no access to Activity's views).
 */
class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) return

        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras
            val pdus = bundle?.get("pdus") as? Array<*>

            if (!pdus.isNullOrEmpty()) {
                val smsBody = StringBuilder()
                var sender: String? = null

                for (pdu in pdus) {
                    val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                    sender = sender ?: smsMessage.displayOriginatingAddress
                    smsBody.append(smsMessage.messageBody)
                }

                if (!sender.isNullOrEmpty() && smsBody.isNotEmpty()) {
                    val toastMsg = "SMS from: $sender\nMessage: ${smsBody.toString()}"
                    Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show()

                    val localIntent = Intent(ACTION_SMS_RECEIVED).apply {
                        putExtra(EXTRA_SMS_SENDER, sender)
                        putExtra(EXTRA_SMS_BODY, smsBody.toString())
                    }
                    LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent)
                }
            }
        }
    }

    companion object {
        const val ACTION_SMS_RECEIVED = "com.example.smsreceiverapp.SMS_RECEIVED"
        const val EXTRA_SMS_SENDER = "sms_sender"
        const val EXTRA_SMS_BODY = "sms_body"
    }
}
