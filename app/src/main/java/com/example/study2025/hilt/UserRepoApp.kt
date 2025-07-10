package com.example.study2025.hilt

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Inject
import javax.inject.Qualifier

class LoggerService @Inject constructor() {
    fun print(message: String) {
        println(message)
    }
}

interface UserRepository {
    fun saveUser(email: String, password:String)
}

class SQLRepository @Inject constructor(private val loggerService: LoggerService): UserRepository {
    override fun saveUser(email: String, password: String) {
        loggerService.print("User saved in SQL DB")
    }
}

class FirebaseRepository @Inject constructor(): UserRepository {
    override fun saveUser(email: String, password: String) {
        println("User saved in Firebase")
    }
}

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
annotation class FirebaseQualifier

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
annotation class SQLQualifier

@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {

    @Provides
    @FirebaseQualifier
    fun providesFirebaseRepository(): UserRepository {
        return FirebaseRepository()
    }

    @Provides
    @SQLQualifier
    fun providesSQLRepository(loggerService: LoggerService): UserRepository {
        return SQLRepository(loggerService)
    }
}

@HiltViewModel
class MyViewModel @Inject constructor(
    @FirebaseQualifier private val firebaseRepo: UserRepository,
    @SQLQualifier private val sqlRepo: UserRepository
): ViewModel() {
    fun saveWithFirebase(email: String, password: String) {
        firebaseRepo.saveUser(email, password)
    }

    fun saveWithSQL(email: String, password: String) {
        sqlRepo.saveUser(email, password)
    }
}

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppContent(viewModel)
            }
        }
    }
}

@Composable
fun AppContent(viewModel: MyViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            viewModel.saveWithFirebase("firebase@example.com", "password123")
        }) {
            Text("Save to Firebase")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.saveWithSQL("sql@example.com", "password456")
        }) {
            Text("Save to SQL")
        }
    }
}

@HiltAndroidApp
class MyApplication: Application()








