package com.example.habitflow.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.habitflow.data.Habit

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
            // fondo
            drawRect(
                color = Color.LightGray.copy(alpha = 0.2f),
                size = Size(size.width, size.height)
            )
            // barra
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
