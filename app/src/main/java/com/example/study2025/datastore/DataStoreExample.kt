package com.example.study2025.datastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

// Extension property for DataStore
private val ComponentActivity.dataStore by preferencesDataStore(name = "settings")

class DataStoreExample : ComponentActivity() {

    companion object {
        val THEME_KEY = stringPreferencesKey("theme_key")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            dataStore.data.collect { preferences: Preferences ->
                val theme = preferences[THEME_KEY] ?: "light"
                println("Theme: $theme")
            }
        }
    }
}
