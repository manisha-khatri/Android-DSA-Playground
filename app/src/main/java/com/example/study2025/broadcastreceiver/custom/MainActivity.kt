package com.example.study2025.broadcastreceiver.custom

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.study2025.R

class MainActivity : AppCompatActivity() {

    private lateinit var messageEditText: EditText
    private lateinit var valueEditText: EditText
    private lateinit var sendBroadcastButton: Button
    private lateinit var myReceiver: MyCustomBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_broadcast_receiver)

        // Initialize views
        messageEditText = findViewById(R.id.messageEditText)
        valueEditText = findViewById(R.id.valueEditText)
        sendBroadcastButton = findViewById(R.id.sendBroadcastButton)

        myReceiver = MyCustomBroadcastReceiver()

        sendBroadcastButton.setOnClickListener { sendCustomBroadcast() }
    }

    override fun onStart() {
        super.onStart()
        // Register receiver
        IntentFilter(ACTION_CUSTOM_BROADCAST).apply {
            LocalBroadcastManager.getInstance(this@MainActivity).registerReceiver(myReceiver, this)
        }
        Toast.makeText(this, "Receiver registered!", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        // Unregister receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver)
        Toast.makeText(this, "Receiver unregistered!", Toast.LENGTH_SHORT).show()
    }

    private fun sendCustomBroadcast() {
        val message = messageEditText.text.toString()
        val value = valueEditText.text.toString().toIntOrNull() ?: 0

        Intent(ACTION_CUSTOM_BROADCAST).apply {
            putExtra("message_key", message)
            putExtra("value_key", value)
            LocalBroadcastManager.getInstance(this@MainActivity).sendBroadcast(this)
        }

        Toast.makeText(this, "Custom broadcast sent!", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val ACTION_CUSTOM_BROADCAST = "com.example.study2025.broadcastreceiver.ACTION_CUSTOM_BROADCAST"
    }
}
