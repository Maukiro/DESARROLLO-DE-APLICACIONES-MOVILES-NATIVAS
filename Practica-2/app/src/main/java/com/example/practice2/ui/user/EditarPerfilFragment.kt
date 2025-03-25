package com.example.practice2.ui.user

import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.practice2.R
import com.example.practice2.data.api.ApiUtils
import com.example.practice2.model.Usuario
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarPerfilFragment : Fragment() {

    private lateinit var etNombre: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnGuardar: Button
    private var jwt: String? = null
    private var emailOriginal: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_editar_perfil, container, false)

        etNombre = view.findViewById(R.id.etNombre)
        etEmail = view.findViewById(R.id.etEmail)
        btnGuardar = view.findViewById(R.id.btnGuardar)

        val prefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        jwt = prefs.getString("jwt", null)
        emailOriginal = jwt?.let { decodeEmailFromJWT(it) }

        cargarDatosUsuario()

        btnGuardar.setOnClickListener {
            val nuevoNombre = etNombre.text.toString().trim()
            val nuevoEmail = etEmail.text.toString().trim()

            if (nuevoNombre.isNotEmpty() && nuevoEmail.isNotEmpty()) {
                actualizarDatosUsuario(nuevoNombre, nuevoEmail)
            } else {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun cargarDatosUsuario() {
        if (emailOriginal != null && jwt != null) {
            ApiUtils.apiService.obtenerUsuarioPorEmail(emailOriginal!!, "Bearer $jwt")
                .enqueue(object : Callback<Usuario> {
                    override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                        if (response.isSuccessful && response.body() != null) {
                            val usuario = response.body()!!
                            etNombre.setText(usuario.nombre)
                            etEmail.setText(usuario.email)
                        }
                    }

                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    private fun actualizarDatosUsuario(nombre: String, email: String) {
        ApiUtils.apiService.actualizarNombreCorreo(emailOriginal!!, nombre, email, "Bearer $jwt")
            .enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.nav_perfil) // Regresar al perfil
                    } else {
                        Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun decodeEmailFromJWT(token: String): String {
        return try {
            val parts = token.split(".")
            val payload = parts[1]
            val decoded = String(Base64.decode(payload, Base64.DEFAULT))
            val json = JSONObject(decoded)
            json.getString("sub")
        } catch (e: Exception) {
            ""
        }
    }
}
