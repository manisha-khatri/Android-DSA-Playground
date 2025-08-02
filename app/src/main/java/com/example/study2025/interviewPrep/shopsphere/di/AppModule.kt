package com.example.study2025.interviewPrep.shopsphere.di

import com.example.study2025.interviewPrep.shopsphere.domain.SearchProductUseCase
import com.example.study2025.interviewPrep.shopsphere.domain.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun providesSearchProductUseCase(repository: SearchRepository): SearchProductUseCase {
        return SearchProductUseCase(repository)
    }
}