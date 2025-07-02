package com.example.study2025.navigationcompose

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import androidx.navigation.compose.composable

@HiltAndroidApp
class ComposeNavigationApp() : Application()

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold { innerPadding ->
                Box(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "ScreenA",
        route = "main_graph2" // <- This becomes the NavGraph ID
    ) {
        composable("ScreenA") { backStackEntry ->
            // Safe way to access parent backStackEntry
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("main_graph2")
            }
            val viewModel: SharedViewModel = hiltViewModel(parentEntry)

            ScreenA {
                viewModel.setUserName(it)
                navController.navigate("ScreenB")
            }
        }

        composable("ScreenB") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("main_graph2")
            }
            val viewModel: SharedViewModel = hiltViewModel(parentEntry)

            ScreenB(viewModel = viewModel)
        }
    }
}
