package com.example.study2025.unittests.quotify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.study2025.R

class QuotifyMainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel

    private val quoteText: TextView
        get() = findViewById(R.id.quoteText)

    private val quoteAuthor: TextView
        get() = findViewById(R.id.quoteAuthor)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_quotify)

        mainViewModel = ViewModelProvider(this, MainViewModelFactory(application)).get(MainViewModel::class.java)
        setQuote(mainViewModel.getQuote())
    }

    fun setQuote(quote:Quote){
        quoteText.text = quote.text
        quoteAuthor.text = quote.author
    }

    fun onPrevious(view: View) {
        setQuote(mainViewModel.previousQuote())
    }

    fun onNext(view: View) {
        setQuote(mainViewModel.nextQuote())
    }

    fun onShare(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, mainViewModel.getQuote().text)
        startActivity(intent)
    }


}