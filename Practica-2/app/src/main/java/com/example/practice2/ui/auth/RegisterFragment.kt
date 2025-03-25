package com.example.practice2.ui.auth

import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.practice2.R
import com.example.practice2.data.api.ApiUtils
import com.example.practice2.model.LoginRequest
import com.example.practice2.model.RegistroUsuarioDTO
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {

    private lateinit var etNombre: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegistrar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        etNombre = view.findViewById(R.id.etNombre)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        btnRegistrar = view.findViewById(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registrarUsuario(nombre, email, password)
        }
        val btnIrALogin = view.findViewById<Button>(R.id.btnIrALogin)
        btnIrALogin.setOnClickListener {
            findNavController().navigate(R.id.nav_login)
        }


        return view
    }

    private fun registrarUsuario(nombre: String, email: String, password: String) {
        val nuevoUsuario = RegistroUsuarioDTO(nombre, email, password)

        ApiUtils.apiService.register(nuevoUsuario, "USER").enqueue(object : Callback<com.example.practice2.model.Usuario> {
            override fun onResponse(
                call: Call<com.example.practice2.model.Usuario>,
                response: Response<com.example.practice2.model.Usuario>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
                    // Hacer login automático
                    login(email, password)
                } else {
                    Toast.makeText(requireContext(), "Error al registrar", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<com.example.practice2.model.Usuario>, t: Throwable) {
                Toast.makeText(requireContext(), "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun login(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        ApiUtils.apiService.login(loginRequest).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()!!

                    val prefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
                    prefs.edit().putString("jwt", token).apply()

                    val rol = decodeRoleFromJWT(token)

                    when (rol) {
                        "ADMIN" -> findNavController().navigate(R.id.nav_admin)
                        "USER" -> findNavController().navigate(R.id.nav_perfil)
                        else -> Toast.makeText(requireContext(), "Rol desconocido", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Login fallido", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
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
}
