package com.example.study2025.architecture.mvp.ui

import com.example.study2025.architecture.mvp.data.NotesRepository

class AddNotePresenter(
    private var view: AddNoteContract.View?,
    private val repository: NotesRepository
) : AddNoteContract.Presenter {

    override fun onSaveClicked(title: String, content: String) {
        if (title.isBlank() || content.isBlank()) {
            view?.showErrorMessage("Title and content cannot be empty")
            return
        }

        repository.addNote(title, content)
        view?.showSuccessMessage()
        view?.clearFields()
    }

    override fun onDestroy() {
        view = null // avoid memory leaks
    }
}
