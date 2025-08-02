package com.example.study2025.room.withouthilt

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity: AppCompatActivity() {

    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = UserDatabase.getDatabase(applicationContext)
        val userDao = database.userDao()

        val userRepository = UserRepository(userDao)

        val factory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, factory)[userViewModel::class.java]

        lifecycleScope.launch {
            userViewModel.insertUser(User(firstName = "Alice", lastName = "Smith"))
            userViewModel.insertUser(User(firstName = "Bob", lastName = "Johnson"))
        }

        // Observe all users
        lifecycleScope.launch {
            userViewModel.getAllUsersOnce().collect { users ->
                Log.d("MainActivity", "Current Users: $users")
                // Update your UI with the list of users
            }
        }

        // Example: Update a user after some delay (for demonstration)
        lifecycleScope.launch {
            kotlinx.coroutines.delay(2000) // Wait 2 seconds
            userViewModel.getUser(1)?.let { userToUpdate ->
                val updatedUser = userToUpdate.copy(firstName = "Alicia")
                userViewModel.updateUser(updatedUser)
            }
        }
    }
}

class UserViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
