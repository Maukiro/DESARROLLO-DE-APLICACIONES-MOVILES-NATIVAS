package com.example.practice2.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.practice2.R
import com.example.practice2.data.api.ApiUtils
import com.example.practice2.model.LoginRequest
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Base64

class LoginFragment : Fragment() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        btnLogin = view.findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email, password)
            } else {
                Toast.makeText(requireContext(), "Completa los campos", Toast.LENGTH_SHORT).show()
            }
        }

        val btnIrARegistro = view.findViewById<Button>(R.id.btnIrARegistro)
        btnIrARegistro.setOnClickListener {
            findNavController().navigate(R.id.nav_register)
        }


        return view
    }

    private fun login(email: String, password: String) {
        val request = LoginRequest(email, password)

        ApiUtils.apiService.login(request).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()!!

                    // Guardar token
                    val prefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
                    prefs.edit().putString("jwt", token).apply()

                    // Decodificar el rol desde el token
                    val role = decodeRoleFromJWT(token)

                    // Redirigir según el rol
                    when (role) {
                        "ADMIN" -> findNavController().navigate(R.id.nav_admin)
                        "USER" -> findNavController().navigate(R.id.nav_perfil)
                        else -> Toast.makeText(requireContext(), "Rol no reconocido", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(requireContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(requireContext(), "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
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
