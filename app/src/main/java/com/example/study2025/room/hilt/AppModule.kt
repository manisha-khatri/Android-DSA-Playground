package com.example.study2025.room.hilt

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesSuggestionDatabase(@ApplicationContext context: Context): SuggestionDatabase {
        return Room.databaseBuilder(
            context,
            SuggestionDatabase::class.java,
            "suggestion_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providesUserDao(suggestionDatabase: SuggestionDatabase): SuggestionDao {
        return suggestionDatabase.suggestionDao()
    }
}