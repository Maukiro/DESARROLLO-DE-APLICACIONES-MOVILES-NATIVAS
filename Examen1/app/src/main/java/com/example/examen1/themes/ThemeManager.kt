package com.example.examen1.themes

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.example.examen1.R

object ThemeManager {

    fun applyTheme(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val selectedTheme = prefs.getString("app_theme", "guinda")

        val isDarkMode = when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }

        when (selectedTheme) {
            "guinda" -> {
                if (isDarkMode) {
                    context.setTheme(R.style.Theme_Guinda_Dark)
                } else {
                    context.setTheme(R.style.Theme_Guinda_Light)
                }
            }
            "azul" -> {
                if (isDarkMode) {
                    context.setTheme(R.style.Theme_Azul_Dark)
                } else {
                    context.setTheme(R.style.Theme_Azul_Light)
                }
            }
            else -> {
                // Tema por defecto (guinda)
                if (isDarkMode) {
                    context.setTheme(R.style.Theme_Guinda_Dark)
                } else {
                    context.setTheme(R.style.Theme_Guinda_Light)
                }
            }
        }
    }
}