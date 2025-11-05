package com.example.study2025.architecture.mvp.ui

interface AddNoteContract {

    interface View {
        fun showSuccessMessage()
        fun showErrorMessage(message: String)
        fun clearFields()
    }

    interface Presenter {
        fun onSaveClicked(title: String, content: String)
        fun onDestroy() // cleanup if needed
    }
}
