package com.example.study2025.projects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import javax.inject.Inject

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}

data class User(val id: Int, val name: String)

class UserRepository @Inject constructor(private val apiService: ApiService) {
    fun fetchUsers(): Flow<Result<List<User>>> = flow {
        emit(Result.Loading)
        try {
            val users = apiService.getUsers()
            emit(Result.Success(users))
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)
}

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _userState = MutableStateFlow<Result<List<User>>>(Result.Loading)
    val userState: StateFlow<Result<List<User>>> = _userState

    init {
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch {
            repository.fetchUsers().collect {
                _userState.value = it
            }
        }
    }
}


@Composable
fun UserScreen(viewModel: UserViewModel = hiltViewModel()) {
    val state by viewModel.userState.collectAsState()

    when (state) {
        is Result.Loading -> Text("Loading...")
        is Result.Success<*> -> {
            val users = (state as Result.Success<List<User>>).data
            LazyColumn {
                items(users) { user ->
                    Text("User: ${user.name}")
                }
            }
        }
        is Result.Error -> Text("Error: ${(state as Result.Error).message}")
    }
}

@AndroidEntryPoint
class MainActivity17 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserScreen()
        }
    }
}


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/") // fake API
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

