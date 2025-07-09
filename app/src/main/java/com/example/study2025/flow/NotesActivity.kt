package com.example.study2025.flow

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class NotesActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            val time = measureTimeMillis {
                getNotes()
                    .flowOn(Dispatchers.IO) //background thread, Producer runs on background thread
                    .map {
                        FormattedNote(it.isActive, it.title.uppercase(), it.description)
                    }
                    .filter { it.isActive }
                    .buffer(3)
                    .collect {
                        delay(1500)
                        Log.d(FlowFunctionsActivity.Companion.TAG, it.toString())
                    }
            }
            Log.d(FlowFunctionsActivity.Companion.TAG, "time = $time")

        }
    }

    // Producer
    fun getNotes(): Flow<Note> {
        val list = listOf(
            Note(1, true, "Kotlin Basics", "Learn about data classes and functions"),
            Note(2, false, "Coroutines", "Understand how to write asynchronous code"),
            Note(3, true, "Flow Operators", "Explore map, filter, flatMap"),
            Note(4, false, "StateFlow vs SharedFlow", "Master hot and cold streams"),
            Note(5, true, "Compose UI", "Build modern UIs with Jetpack Compose")
        )
        return list.asFlow()
    }


    data class Note(val id: Int, val isActive: Boolean, val title: String, val description: String)
    data class FormattedNote(val isActive: Boolean, val title:String, val description: String)
}