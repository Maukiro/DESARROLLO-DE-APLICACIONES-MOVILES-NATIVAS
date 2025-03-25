package com.example.practice2.ui.admin

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
import com.example.practice2.model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarUsuarioAdminFragment : Fragment() {

    private lateinit var etNombre: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnGuardar: Button

    private var emailOriginal: String? = null
    private var jwt: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_editar_usuario_admin, container, false)

        etNombre = view.findViewById(R.id.etNombreAdmin)
        etEmail = view.findViewById(R.id.etEmailAdmin)
        btnGuardar = view.findViewById(R.id.btnGuardarAdmin)

        val prefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        jwt = prefs.getString("jwt", null)

        emailOriginal = arguments?.getString("emailUsuario")

        cargarDatosUsuario()

        btnGuardar.setOnClickListener {
            val nombreNuevo = etNombre.text.toString().trim()
            val emailNuevo = etEmail.text.toString().trim()

            if (nombreNuevo.isNotEmpty() && emailNuevo.isNotEmpty()) {
                actualizarDatos(nombreNuevo, emailNuevo)
            } else {
                Toast.makeText(requireContext(), "Completa los campos", Toast.LENGTH_SHORT).show()
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

    private fun actualizarDatos(nombre: String, email: String) {
        ApiUtils.apiService.actualizarNombreCorreo(emailOriginal!!, nombre, email, "Bearer $jwt")
            .enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Usuario actualizado", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.nav_admin)
                    } else {
                        Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }
}
