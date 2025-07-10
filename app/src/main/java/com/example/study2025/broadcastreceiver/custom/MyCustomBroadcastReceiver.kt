package com.example.study2025.broadcastreceiver.custom

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyCustomBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action != null && intent.action == MainActivity2.ACTION_CUSTOM_BROADCAST) {
            Log.d(TAG, "Custom broadcast received!")

            val message = intent.getStringExtra("message_key")
            val value = intent.getIntExtra("value_key", 0)

            val toastMessage = "Broadcast Received!\nMessage: " + message + "\nValue: " + value
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val TAG = "CustomReceiver"
    }
}