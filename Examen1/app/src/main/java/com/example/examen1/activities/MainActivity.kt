package com.example.examen1.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.examen1.R
import com.example.examen1.fragments.InicioFragment
import com.example.examen1.fragments.TemasFragment
import com.example.examen1.themes.ThemeManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        // Cargar tema antes del onCreate
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Cargar fragmento inicial
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, InicioFragment())
                .commit()
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, InicioFragment())
                        .commit()
                    true
                }
                R.id.nav_temas -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, TemasFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}
