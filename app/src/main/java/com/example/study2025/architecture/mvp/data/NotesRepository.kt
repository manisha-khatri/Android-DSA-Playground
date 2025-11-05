package com.example.study2025.architecture.mvp.data

/**
 * Repository layer to abstract data operations.
 * Presenter interacts with this instead of directly touching the data source.
 */
class NotesRepository(private val localDataSource: NotesLocalDataSource) {

    fun getAllNotes(): List<Note> {
        return localDataSource.getAllNotes()
    }

    fun addNote(title: String, content: String): Note {
        return localDataSource.addNote(title, content)
    }

    fun deleteNote(noteId: Int): Boolean {
        return localDataSource.deleteNote(noteId)
    }

    fun getNoteById(noteId: Int): Note? {
        return localDataSource.getNoteById(noteId)
    }

    fun clearAll() {
        localDataSource.clearAllNotes()
    }
}
