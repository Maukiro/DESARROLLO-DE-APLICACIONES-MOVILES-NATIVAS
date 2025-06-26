package com.example.habitflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitflow.data.Habit
import com.example.habitflow.ui.components.HabitCard
import com.example.habitflow.viewmodel.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HabitViewModel
) {
    val habitList by viewModel.habitList.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("add")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar hábito")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Mis Hábitos") }
            )
        }
    ) { padding ->
        if (habitList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Todavía no tienes hábitos. ¡Empieza creando uno!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(habitList) { habit ->
                    HabitCard(
                        habit = habit,
                        onClick = {},
                        onToggleCompleted = {
                            viewModel.updateHabit(
                                habit.copy(isCompletedToday = !habit.isCompletedToday)
                            )
                        }
                    )
                }
            }
        }
    }
}