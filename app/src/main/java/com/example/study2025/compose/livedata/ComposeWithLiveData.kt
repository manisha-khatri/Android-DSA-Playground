package com.example.study2025.compose.livedata

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class UserResponse(val id: Int, val name: String)

interface ApiService {
    @GET("users/1")
    suspend fun getUser(): UserResponse

    companion object {
        val api: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}

//--------------------LIVEDATA-------------------------
class Event<out T>(private val content: T) {
    private var handled = false
    fun getContentIfNotHandled(): T? =
        if (handled) null else {
            handled = true
            content
        }
}

class UserViewModel: ViewModel() {
    private val _user = MutableLiveData<String>()
    val user: LiveData<String> = _user

    private val _navigateEvent = MutableLiveData<Event<Unit>>()
    val navigateEvent: LiveData<Event<Unit>> = _navigateEvent

    fun fetchUser() {
        viewModelScope.launch {
            _user.postValue("Loading..")
            try {
                val response = ApiService.api.getUser()
                _user.postValue(response.name)
            } catch (e: Exception) {
                _user.postValue("Error..")
            }
        }
    }

    fun onClickNavigate() {
        viewModelScope.launch {
            _navigateEvent.value = Event(Unit)
        }
    }
}

@Composable
fun UserScreenLiveData(
    vm: UserViewModel = viewModel(),
    navigation: () -> Unit // callback function
) {
    val userState by vm.user.observeAsState("Tap Button")
    val navEvent by vm.navigateEvent.observeAsState()

    LaunchedEffect(navEvent) {
        navEvent?.getContentIfNotHandled()?.let {
            navigation()
        }
    }

    Column(
        modifier = Modifier.padding(16.dp),
    ) {
        Text(text = userState)

        Button(onClick = { vm.fetchUser() }) {
            Text("Fetch User")
        }

        Button(onClick = { vm.onClickNavigate() }) {
            Text("Go Next (LiveData)")
        }
    }
}

//--------------------LIVEDATA-------------------------


//--------------------STATEFLOW-------------------------
class UserVM: ViewModel() {
    private val _user = MutableStateFlow("Tap Button")
    val user: StateFlow<String> = _user

    private val _navigate = MutableSharedFlow<Unit>()
    val navigate: SharedFlow<Unit> = _navigate

    fun fetchUser() {
        viewModelScope.launch {
            _user.value = "Loading.."
            try {
                val response = ApiService.api.getUser()
                _user.value = response.name
            } catch (e: Exception) {
                _user.value = "Error.."
            }
        }
    }

    fun triggerNavigation() {
        viewModelScope.launch {
            _navigate.emit(Unit)
        }
    }
}

@Composable
fun UserScreenStateFlow(
    vm: UserVM = viewModel(),
    navigate: () -> Unit
) {
    val userState by vm.user.collectAsState()

    LaunchedEffect(Unit) {
        vm.navigate.collect {
            navigate()
        }
    }

    Column(
        modifier = Modifier.padding(16.dp),
    ) {
        Text(text = userState)

        Button(onClick = { vm.fetchUser() }) {
            Text("Fetch User")
        }

        Button(onClick = { vm.triggerNavigation() }) {
            Text("Go Next (StateFlow)")
        }
    }
}

//--------------------STATEFLOW-------------------------

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "flow") {

        composable("live") {
            UserScreenLiveData {
                navController.navigate("details")
            }
        }

        composable("flow") {
            UserScreenStateFlow {
                navController.navigate("details")
            }
        }

        composable("details") {
            Text("Details Screen", fontSize = 28.sp, modifier = Modifier.padding(32.dp))
        }
    }
}

@AndroidEntryPoint
class MainActivity101: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNav()
        }
    }
}

@HiltAndroidApp
class ProductApp: Application()

