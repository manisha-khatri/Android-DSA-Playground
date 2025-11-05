package com.example.study2025.architecture.mvp.data


/**
 * Simulates local storage for notes using an in-memory list.
 * In real-world, this would connect to Room or SQLite.
 */
class NotesLocalDataSource {

    private val notes = mutableListOf<Note>()
    private var nextId = 1

    fun getAllNotes(): List<Note> {
        return notes.sortedByDescending { it.timestamp }
    }

    fun addNote(title: String, content: String): Note {
        val note = Note(
            id = nextId++,
            title = title,
            content = content
        )
        notes.add(note)
        return note
    }

    fun deleteNote(noteId: Int): Boolean {
        return notes.removeIf { it.id == noteId }
    }

    fun getNoteById(noteId: Int): Note? {
        return notes.find { it.id == noteId }
    }

    fun clearAllNotes() {
        notes.clear()
        nextId = 1
    }
}
