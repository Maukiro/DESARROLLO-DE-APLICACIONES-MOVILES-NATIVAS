package com.example.push_notification

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotificationHistoryActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var rvNotificaciones: RecyclerView
    private lateinit var adapter: NotificacionAdapter
    private val listaNotificaciones = mutableListOf<Notificacion>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_history)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        rvNotificaciones = findViewById(R.id.rvNotificaciones)
        rvNotificaciones.layoutManager = LinearLayoutManager(this)
        adapter = NotificacionAdapter(listaNotificaciones)
        rvNotificaciones.adapter = adapter

        cargarNotificaciones()
    }

    private fun cargarNotificaciones() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("notificaciones")
            .whereEqualTo("destinatario", userId)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                listaNotificaciones.clear()
                for (document in result) {
                    val noti = document.toObject(Notificacion::class.java)
                    listaNotificaciones.add(noti)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar notificaciones: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
