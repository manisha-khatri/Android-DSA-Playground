package com.example.study2025.interviewPrep.shopsphere.data

import com.example.study2025.interviewPrep.shopsphere.domain.SearchSuggestion
import com.example.study2025.retrofit.withhilt.RemoteDataSource
import com.example.study2025.room.hilt.LocalDataSource
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    val localDataSource: LocalDataSource,
    val remoteDataSource: RemoteDataSource
) {
    suspend fun getSearchSuggestions(query: String): List<SearchSuggestion> {
        val cached = localDataSource.getSuggestions(query)
        if(cached.isNotEmpty()) {
            return cached.map { SearchSuggestion(it.searchQuery) }
        }

        val remoteSuggestions = remoteDataSource.getUsersByLastName(query)
        if(remoteSuggestions.isNotEmpty()) {
            return remoteSuggestions.map {
                SearchSuggestion(it.lastName)
            }
        }

        return emptyList()
    }
}