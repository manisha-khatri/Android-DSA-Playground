package com.example.study2025.coroutine.whycoroutine

fun downloadFile(url: String, callback: (String) -> Unit) {
    // Simulate downloading
    callback("FileContent")
}

fun processFile(content: String, callback: (Boolean) -> Unit) {
    callback(true)
}

fun saveToDatabase(success: Boolean, callback: (String) -> Unit) {
    callback("Saved Successfully")
}

fun main() {
    downloadFile("http://file", { content ->
        processFile(content, { processed ->
            saveToDatabase(processed, { result ->
                println(result)  // So many layers!
            })
        })
    })
}