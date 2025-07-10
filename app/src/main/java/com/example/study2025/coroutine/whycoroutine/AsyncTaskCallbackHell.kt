package com.example.study2025.coroutine.whycoroutine

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity8 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DownloadFileTask().execute("http://file")
    }

    inner class DownloadFileTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            // Simulate download
            Thread.sleep(1000)
            return "FileContent"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            // Nested AsyncTask: here we go...
            ProcessFileTask().execute(result)
        }
    }

    inner class ProcessFileTask : AsyncTask<String, Void, Boolean>() {
        override fun doInBackground(vararg params: String?): Boolean {
            // Simulate processing
            Thread.sleep(1000)
            return true
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            // Another nest inside the nest
            SaveToDatabaseTask().execute(result)
        }
    }

    inner class SaveToDatabaseTask : AsyncTask<Boolean, Void, String>() {
        override fun doInBackground(vararg params: Boolean?): String {
            // Simulate database save
            Thread.sleep(1000)
            return "Saved Successfully"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            println(result)
        }
    }
}
