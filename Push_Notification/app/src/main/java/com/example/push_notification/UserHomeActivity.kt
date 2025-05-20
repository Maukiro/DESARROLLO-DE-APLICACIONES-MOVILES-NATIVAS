package com.example.push_notification

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class UserHomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)
        val btnVerPerfil = findViewById<Button>(R.id.btnVerPerfil)
        val btnHistorial = findViewById<Button>(R.id.btnHistorialNotificaciones)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // Cargar el nombre del usuario autenticado
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("usuarios").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nombre = document.getString("nombre") ?: "Usuario"
                        tvBienvenida.text = "Bienvenido, $nombre"
                    }
                }
        }

        btnVerPerfil.setOnClickListener {
            // Abrir Activity para ver/editar perfil
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        btnHistorial.setOnClickListener {
            // Abrir Activity para historial de notificaciones
            startActivity(Intent(this, NotificationHistoryActivity::class.java))
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Guarda o actualiza el token FCM del usuario en Firestore
        guardarToken()
    }

    // Método para guardar el token FCM en Firestore
    private fun guardarToken() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    FirebaseFirestore.getInstance().collection("usuarios")
                        .document(userId)
                        .update("fcmToken", token)
                }
            }
    }
}
