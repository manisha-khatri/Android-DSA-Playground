package com.example.study2025.interviewPrep.shopsphere.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.example.study2025.interviewPrep.shopsphere.domain.SearchProductUseCase
import com.example.study2025.interviewPrep.shopsphere.presentation.SearchEvent.ClearQuery
import com.example.study2025.interviewPrep.shopsphere.presentation.SearchEvent.OnRetrySearch
import com.example.study2025.interviewPrep.shopsphere.presentation.SearchEvent.QueryChanged
import com.example.study2025.interviewPrep.shopsphere.presentation.SearchEvent.SuggestionClicked
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchUseCase: SearchProductUseCase): ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState
    private var searchJob: Job ?= null

    fun onEvent(event: SearchEvent) {
        when(event) {
            is QueryChanged -> {
                _uiState.update { it.copy(query = event.query, isLoading = true) }

                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    try {
                        val suggestions = searchUseCase(event.query)
                        _uiState.update {
                            it.copy(
                                suggestions = suggestions,
                                isLoading = false,
                                error = null
                            )
                        }
                    } catch (e: Exception) {
                        _uiState.update {
                            it.copy(isLoading = false, error = e.message)
                        }
                    }

                }
            }
            is ClearQuery -> TODO()
            is OnRetrySearch -> TODO()
            is SuggestionClicked -> TODO()
        }
    }
}