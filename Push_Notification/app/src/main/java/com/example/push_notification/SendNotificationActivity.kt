package com.example.push_notification

import Usuario
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class SendNotificationActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: UsuarioSelectAdapter
    private val listaUsuarios = mutableListOf<Usuario>()
    private val selectedUids = mutableSetOf<String>()
    private val tokensSeleccionados = mutableListOf<String>()

    // Coloca tu Server Key aquí
    private val SERVER_KEY = "AAAA...TuServerKey..." // OBTENLA de Firebase Console

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_notification)

        db = FirebaseFirestore.getInstance()

        val etTitulo = findViewById<EditText>(R.id.etTitulo)
        val etMensaje = findViewById<EditText>(R.id.etMensaje)
        val rvUsuariosSelect = findViewById<RecyclerView>(R.id.rvUsuariosSelect)
        val btnEnviar = findViewById<Button>(R.id.btnEnviar)

        rvUsuariosSelect.layoutManager = LinearLayoutManager(this)
        adapter = UsuarioSelectAdapter(listaUsuarios, selectedUids)
        rvUsuariosSelect.adapter = adapter

        cargarUsuarios()

        btnEnviar.setOnClickListener {
            val titulo = etTitulo.text.toString().trim()
            val mensaje = etMensaje.text.toString().trim()
            if (titulo.isEmpty() || mensaje.isEmpty() || selectedUids.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos y selecciona destinatarios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            obtenerTokensYEnviar(titulo, mensaje)
        }
    }

    private fun cargarUsuarios() {
        db.collection("usuarios").get()
            .addOnSuccessListener { result ->
                listaUsuarios.clear()
                for (document in result) {
                    val usuario = document.toObject(Usuario::class.java).copy(uid = document.id)
                    // No mostrar al admin actual
                    listaUsuarios.add(usuario)
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun obtenerTokensYEnviar(titulo: String, mensaje: String) {
        tokensSeleccionados.clear()
        db.collection("usuarios").whereIn(FieldPath.documentId(), selectedUids.toList()).get()
            .addOnSuccessListener { result ->
                val destinatariosFirestore = mutableListOf<String>()
                for (document in result) {
                    val token = document.getString("fcmToken")
                    if (!token.isNullOrEmpty()) {
                        tokensSeleccionados.add(token)
                        destinatariosFirestore.add(document.id)
                    }
                }
                if (tokensSeleccionados.isNotEmpty()) {
                    enviarNotificaciones(tokensSeleccionados, titulo, mensaje)
                    guardarNotificacionesFirestore(destinatariosFirestore, titulo, mensaje)
                } else {
                    Toast.makeText(this, "No se encontraron tokens válidos", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun enviarNotificaciones(tokens: List<String>, titulo: String, mensaje: String) {
        for (token in tokens) {
            val client = OkHttpClient()
            val json = JSONObject()
            val notification = JSONObject()
            notification.put("title", titulo)
            notification.put("body", mensaje)
            json.put("to", token)
            json.put("notification", notification)
            val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json.toString())
            val request = Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .addHeader("Authorization", "key=$SERVER_KEY")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@SendNotificationActivity, "Error al enviar notificación: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    // Puedes manejar el resultado si quieres
                }
            })
        }
        runOnUiThread {
            Toast.makeText(this, "Notificación enviada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarNotificacionesFirestore(destinatarios: List<String>, titulo: String, mensaje: String) {
        val db = FirebaseFirestore.getInstance()
        val now = System.currentTimeMillis()
        for (uid in destinatarios) {
            val data = hashMapOf(
                "titulo" to titulo,
                "mensaje" to mensaje,
                "timestamp" to now,
                "destinatario" to uid
            )
            db.collection("notificaciones").add(data)
        }
    }
}
