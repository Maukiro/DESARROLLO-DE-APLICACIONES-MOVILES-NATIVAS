package com.example.practice2.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.practice2.R
import com.example.practice2.data.api.ApiUtils
import com.example.practice2.model.RegistroUsuarioDTO
import com.example.practice2.model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.navigation.fragment.findNavController


class CrearUsuarioAdminFragment : Fragment() {

    private lateinit var etNombre: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etRol: EditText
    private lateinit var btnRegistrar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crear_usuario_admin, container, false)

        etNombre = view.findViewById(R.id.etNombreNuevo)
        etEmail = view.findViewById(R.id.etEmailNuevo)
        etPassword = view.findViewById(R.id.etPasswordNuevo)
        etRol = view.findViewById(R.id.etRolNuevo)
        btnRegistrar = view.findViewById(R.id.btnRegistrarNuevo)

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val rol = etRol.text.toString().trim().uppercase()

            if (nombre.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && rol.isNotEmpty()) {
                registrarUsuario(nombre, email, password, rol)
            } else {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun registrarUsuario(nombre: String, email: String, password: String, rol: String) {
        val nuevoUsuario = RegistroUsuarioDTO(nombre, email, password)

        ApiUtils.apiService.register(nuevoUsuario, rol)
            .enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Usuario registrado", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.nav_admin)
                    } else {
                        Toast.makeText(requireContext(), "Error al registrar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }
}
