package com.example.practice2

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.practice2.databinding.ActivityMainBinding
import org.json.JSONObject
import android.util.Base64

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_login,
                R.id.nav_register,
                R.id.nav_admin,
                R.id.nav_perfil,
                R.id.nav_logout
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // ✅ Manejamos el clic en los ítems del menú
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    // 🧹 Limpiar JWT
                    val prefs = getSharedPreferences("session", Context.MODE_PRIVATE)
                    prefs.edit().remove("jwt").apply()

                    Toast.makeText(this, "Sesión cerrada con éxito", Toast.LENGTH_SHORT).show()

                    // Redirigir al login
                    navController.navigate(R.id.nav_login)

                    // 🔄 Recargar para actualizar el menú
                    recreate()
                    true
                }
                else -> {
                    val handled = androidx.navigation.ui.NavigationUI.onNavDestinationSelected(menuItem, navController)
                    if (handled) binding.drawerLayout.closeDrawers()
                    handled
                }
            }
        }

        // ✅ Persistencia de sesión
        val prefs = getSharedPreferences("session", Context.MODE_PRIVATE)
        val token = prefs.getString("jwt", null)

        if (token != null) {
            val rol = decodeRoleFromJWT(token)

            // 🔐 Mostrar/Ocultar ítems del menú según el rol
            val menu = navView.menu
            if (rol == "USER") {
                menu.findItem(R.id.nav_admin)?.isVisible = false
            } else if (rol == "ADMIN") {
                menu.findItem(R.id.nav_register)?.isVisible = false
                menu.findItem(R.id.nav_login)?.isVisible = false
            }

            // Ocultar login y registro si ya hay sesión
            menu.findItem(R.id.nav_login)?.isVisible = false
            menu.findItem(R.id.nav_register)?.isVisible = false

            // Redirigir automáticamente
            when (rol) {
                "ADMIN" -> navController.navigate(R.id.nav_admin)
                "USER" -> navController.navigate(R.id.nav_perfil)
            }
        }
    }

    private fun decodeRoleFromJWT(token: String): String {
        return try {
            val parts = token.split(".")
            val payload = parts[1]
            val decoded = String(Base64.decode(payload, Base64.DEFAULT))
            val json = JSONObject(decoded)
            json.getString("role")
        } catch (e: Exception) {
            "UNKNOWN"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
