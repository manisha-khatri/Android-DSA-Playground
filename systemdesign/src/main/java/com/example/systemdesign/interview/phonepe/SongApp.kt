package com.example.systemdesign.interview.phonepe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.http.GET

/**
You need to Implement a Song Android App
    - It will show you list of categories
    - If you tap on any of the category, it will land to the details of that particular category page.
    - So there's a button in that page, which is play button
    - So when you tap on it, it will play the random song

Right now just share the algorithm of the picking Random song from the list of songs
Note: it should pick in a way that each song is picked only once and when the size gets
over means all songs are picked randomly once then start picking again
 */
// --------------------------Data------------------------
interface SongApiService {
    @GET("categories")
    suspend fun getCategories(): List<Category>

    @GET("songByCategories")
    suspend fun getSongByCategories(@retrofit2.http.Query("categoryId") categoryId: String) : List<Song>
}

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String
)

@Entity(tableName = "song")
data class SongEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val audio: String,
    val categoryId: String
)

@Dao
interface SongDao {
    @Query("SELECT * FROM category")
    fun getCategories(): Flow<List<Category>>

    @Insert
    suspend fun saveCategory(categories: List<Category>)

    @Query("SELECT * FROM song")
    fun getSongs(categoryId: String): Flow<List<Song>>

    @Insert
    suspend fun saveSongs(songs: List<Song>, categoryId: String)
}

class SongRepository(val dao: SongDao, val api: SongApiService) {
    fun getCategories(): Flow<List<Category>> =
        dao.getCategories()

    suspend fun refreshCategories() {
        val response = api.getCategories()
        dao.saveCategory(response)
    }
}

// --------------------------Domain------------------------
class SongShuffler(songs: List<Song>) {
    val originalSongs = songs
    var shuffledSongs = songs.shuffled()
    var curIndex = 0

    fun next(): Song {
        if(curIndex == originalSongs.size) {
            shuffledSongs.shuffled()
            curIndex = 0
        }
        val song = shuffledSongs[curIndex]
        curIndex++
        return song
    }
}

data class Category(
    val id: String,
    val name: String
)

data class Song(
    val id: String,
    val title: String,
    val audio: String,
    val categoryId: String
)

// --------------------------Presentation------------------------

class SongViewModel(repo: SongRepository): ViewModel() {
    val uiState: StateFlow<List<Category>> =
        repo.getCategories()
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                emptyList()
            )

    init {
        viewModelScope.launch {
            repo.refreshCategories()
        }
    }
}