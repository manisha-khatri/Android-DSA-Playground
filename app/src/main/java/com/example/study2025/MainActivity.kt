package com.example.study2025

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.study2025.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Fragment1.OnFragmentInteractionListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        viewModel.data.observe(this) { data ->
            binding.tv.text = data
            Toast.makeText(this, "Data received: $data", Toast.LENGTH_SHORT).show()
        }

        val fragment1 = Fragment1()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment1)
            .commit()
    }


    override fun onDataReceived(data: String) {
        binding.tv.text = data
        Toast.makeText(this, "Data received: $data", Toast.LENGTH_SHORT).show()
    }
}