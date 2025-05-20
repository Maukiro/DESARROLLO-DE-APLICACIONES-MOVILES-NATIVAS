package com.example.push_notification

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterAdminActivity : AppCompatActivity() {

    private val MASTER_PASSWORD = "admin" // <-- Cambia esto por una contraseña fuerte

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_admin)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etMasterPassword = findViewById<EditText>(R.id.etMasterPassword)
        val btnRegisterAdmin = findViewById<Button>(R.id.btnRegisterAdmin)

        btnRegisterAdmin.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val masterPassword = etMasterPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.length < 6 || masterPassword.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (masterPassword != MASTER_PASSWORD) {
                Toast.makeText(this, "Contraseña maestra incorrecta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Registro con Firebase Auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: ""
                        val userMap = hashMapOf(
                            "nombre" to name,
                            "email" to email,
                            "rol" to "admin"
                        )
                        db.collection("usuarios").document(userId).set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Administrador registrado", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error al guardar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
