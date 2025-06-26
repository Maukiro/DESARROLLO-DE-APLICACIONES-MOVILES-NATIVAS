package com.example.habitflow

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.habitflow.navigation.AppNavigation
import com.example.habitflow.ui.theme.HabitFlowTheme
import com.example.habitflow.viewmodel.HabitViewModel
import com.example.habitflow.viewmodel.HabitViewModelFactory
import android.Manifest


class MainActivity : ComponentActivity() {

    // Inicializa el ViewModel usando tu fábrica personalizada
    private val viewModel: HabitViewModel by viewModels {
        HabitViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HabitFlowTheme {
                AppNavigation(viewModel)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

    }
}