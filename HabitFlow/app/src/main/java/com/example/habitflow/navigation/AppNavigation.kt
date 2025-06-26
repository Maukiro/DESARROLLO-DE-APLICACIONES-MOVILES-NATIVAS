package com.example.habitflow.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.habitflow.ui.screens.*
import com.example.habitflow.viewmodel.HabitViewModel
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(viewModel: HabitViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf("home", "add", "stats")) {
                BottomBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("welcome") {
                WelcomeScreen(navController)
            }
            composable("home") {
                HomeScreen(navController, viewModel)
            }
            composable("add") {
                AddHabitScreen(navController, viewModel)
            }
            composable("stats") {
                StatsScreen(navController, viewModel)
            }
        }
    }
}