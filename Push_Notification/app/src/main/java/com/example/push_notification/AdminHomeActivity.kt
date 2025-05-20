package com.example.push_notification

import Usuario
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class AdminHomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var rvUsuarios: RecyclerView
    private lateinit var adapter: UsuarioAdapter
    private val listaUsuarios = mutableListOf<Usuario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        rvUsuarios = findViewById(R.id.rvUsuarios)
        rvUsuarios.layoutManager = LinearLayoutManager(this)
        adapter = UsuarioAdapter(listaUsuarios)
        rvUsuarios.adapter = adapter

        cargarUsuarios()

        // Botón para cerrar sesión
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Botón para enviar notificación
        val btnSendNotification = findViewById<Button>(R.id.btnSendNotification)
        btnSendNotification.setOnClickListener {
            // Aquí vas a abrir la pantalla para enviar notificaciones
            startActivity(Intent(this, SendNotificationActivity::class.java))
            Toast.makeText(this, "Funcionalidad próximamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarUsuarios() {
        db.collection("usuarios").get()
            .addOnSuccessListener { result ->
                listaUsuarios.clear()
                for (document in result) {
                    val usuario = document.toObject(Usuario::class.java).copy(uid = document.id)
                    listaUsuarios.add(usuario)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al cargar usuarios: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

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

