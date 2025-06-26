package com.example.habitflow.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitflow.data.Habit
import com.example.habitflow.viewmodel.HabitViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    navController: NavController,
    viewModel: HabitViewModel
) {
    val habits by viewModel.habitList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Estadísticas") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Hábitos completados hoy:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${habits.count { it.isCompletedToday }} de ${habits.size}",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Resumen visual:",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Simulación: barras por hábito
            BarChart(habits)
        }
    }
}
@Composable
fun BarChart(habits: List<Habit>) {
    val total = habits.size.coerceAtLeast(1)
    val completed = habits.count { it.isCompletedToday }

    val completionRate = completed.toFloat() / total

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Fondo gris claro
            drawRect(
                color = Color.LightGray.copy(alpha = 0.2f),
                size = Size(size.width, size.height)
            )
            // Barra verde de progreso
            drawRect(
                color = Color(0xFF4CAF50),
                size = Size(size.width * completionRate, size.height)
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "${(completionRate * 100).toInt()}% completado",
        style = MaterialTheme.typography.bodyLarge
    )
}