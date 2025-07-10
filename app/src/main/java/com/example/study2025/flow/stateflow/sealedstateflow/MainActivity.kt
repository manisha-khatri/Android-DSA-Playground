package com.example.study2025.flow.stateflow.sealedstateflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity10: ComponentActivity() {

    val viewModel: DessertViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SetUpData()
        }
    }

    @Composable
    fun SetUpData() {
        val state = viewModel.desertState.collectAsStateWithLifecycle()

        when (val uiState = state.value) {
            is UiState.Loading -> {
                Text(text = "Loading...")
            }

            is UiState.Success -> {
                val res = uiState.data.joinToString(", ")
                Text(text = res)
            }

            is UiState.Failure -> {
                Text(text = "Error occurred")
            }
        }
    }

    fun setUpDataWithCoroutine() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.desertState.collect { state ->
                    when(state) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            val res = state.data.joinToString(" ")
                            //ShowSuccessfulResult(res)
                        }
                        is UiState.Failure -> {}
                    }
                }
            }
        }
    }
}