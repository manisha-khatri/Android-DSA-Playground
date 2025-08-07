package com.example.study2025.interviewPrep.shopsphere.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Routes {
    const val SEARCH = "search"
    const val PRODUCT_LIST = "product_list"
}

@Composable
fun ShopSphereNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Routes.SEARCH
    ) {
        composable(Routes.SEARCH) {
            SearchScreen()
        }
        composable(Routes.PRODUCT_LIST) {

        }
    }

}