package com.example.practice2.ui.admin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practice2.R
import com.example.practice2.data.api.ApiUtils
import com.example.practice2.model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.navigation.fragment.findNavController


class AdminFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UsuariosAdapter
    private val listaUsuarios = mutableListOf<Usuario>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_admin, container, false)
        recyclerView = view.findViewById(R.id.recyclerUsuarios)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = UsuariosAdapter(
            listaUsuarios,
            onEditarClick = { usuario ->
                val bundle = Bundle().apply {
                    putString("emailUsuario", usuario.email)
                }
                findNavController().navigate(R.id.nav_editar_usuario_admin, bundle)
            },
            onEliminarClick = { usuario ->
                eliminarUsuario(usuario.id) // ✅ CORRECTO
            }
        )
        recyclerView.adapter = adapter

        val fab: View = view.findViewById(R.id.fabAgregarUsuario)
        fab.setOnClickListener {
            findNavController().navigate(R.id.nav_crear_usuario_admin)
        }

        obtenerUsuarios()

        return view

    }

    private fun obtenerUsuarios() {
        val prefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        val token = prefs.getString("jwt", null)

        if (token != null) {
            ApiUtils.apiService.obtenerTodosLosUsuarios("Bearer $token")
                .enqueue(object : Callback<List<Usuario>> {
                    override fun onResponse(
                        call: Call<List<Usuario>>,
                        response: Response<List<Usuario>>
                    ) {
                        if (response.isSuccessful) {
                            listaUsuarios.clear()
                            listaUsuarios.addAll(response.body() ?: emptyList())
                            adapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(requireContext(), "Error al obtener usuarios", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    private fun eliminarUsuario(id: Long) { // ✅ CAMBIO AQUÍ
        val prefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        val token = prefs.getString("jwt", null)

        if (token != null) {
            ApiUtils.apiService.eliminarUsuario(id, "Bearer $token")
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Usuario eliminado", Toast.LENGTH_SHORT).show()
                            obtenerUsuarios()
                        } else {
                            Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }


}

