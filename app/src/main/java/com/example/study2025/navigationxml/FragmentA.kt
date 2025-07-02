package com.example.study2025

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.findNavController

class FragmentA : Fragment() {

    val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btn = view.findViewById<Button>(R.id.goToBButton)
        val editTxt = view.findViewById<EditText>(R.id.usernameEditText)

        btn.setOnClickListener {
            val name = editTxt.text.toString()

            viewModel.setUserName(name)

            findNavController().navigate(R.id.action_fragmentA_to_fragmentB)
        }
    }
}