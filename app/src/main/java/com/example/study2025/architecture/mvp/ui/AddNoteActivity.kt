package com.example.study2025.architecture.mvp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.study2025.R
import com.example.study2025.architecture.mvp.data.NotesLocalDataSource
import com.example.study2025.architecture.mvp.data.NotesRepository

class AddNoteActivity : AppCompatActivity(), AddNoteContract.View {

    private lateinit var presenter: AddNoteContract.Presenter
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        btnSave = findViewById(R.id.btnSave)

        // Create dependencies manually (for now)
        val repository = NotesRepository(NotesLocalDataSource())
        presenter = AddNotePresenter(this, repository)

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()
            presenter.onSaveClicked(title, content)
        }
    }

    override fun showSuccessMessage() {
        Toast.makeText(this, "Note added successfully!", Toast.LENGTH_SHORT).show()
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun clearFields() {
        etTitle.text.clear()
        etContent.text.clear()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
