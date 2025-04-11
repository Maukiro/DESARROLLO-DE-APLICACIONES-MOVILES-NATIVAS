package com.example.filemanagerapp.model

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.filemanagerapp.R

object ThemeHelper {
    const val PREFS_NAME = "theme_prefs"
    const val KEY_THEME = "selected_theme"
    const val THEME_GUINDA = "guinda"
    const val THEME_AZUL = "azul"

    fun applyTheme(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        when (prefs.getString(KEY_THEME, THEME_GUINDA)) {
            THEME_GUINDA -> context.setTheme(R.style.Theme_FileManagerApp_Guinda)
            THEME_AZUL -> context.setTheme(R.style.Theme_FileManagerApp_Azul)
            else -> context.setTheme(R.style.Theme_FileManagerApp_Guinda)
        }
    }

    fun saveTheme(context: Context, theme: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_THEME, theme).apply()
    }
}
