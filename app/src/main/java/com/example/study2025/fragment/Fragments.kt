package com.example.study2025.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.study2025.R
import com.example.study2025.databinding.Fragment1Binding

class Fragment1: Fragment(R.layout.fragment1) {

    private lateinit var binding: Fragment1Binding
    private lateinit var viewModel: SharedViewModel

    // Method 2: callback interface
    interface OnFragmentInteractionListener {
        fun onDataReceived(data: String)
    }

    private var listener: OnFragmentInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        binding = Fragment1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sendToActivity()
    }

    private fun sendToActivity() {
        viewModel.data.value = "Data from Fragment through ViewModel"
        //listener?.onDataReceived("Hello from Fragment")
    }

}