package com.example.practice2.ui.user

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.practice2.R
import com.example.practice2.data.api.ApiUtils
import com.example.practice2.model.Usuario
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilFragment : Fragment() {

    private lateinit var tvNombre: TextView
    private lateinit var tvEmail: TextView
    private lateinit var ivFotoPerfil: ImageView
    private lateinit var btnCambiarFoto: Button
    private lateinit var btnEditarPerfil: Button

    private var emailUsuario: String? = null
    private var jwt: String? = null

    private val seleccionarImagen = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            ivFotoPerfil.setImageURI(it)
            if (emailUsuario != null && jwt != null) {
                subirImagenAlServidor(it, emailUsuario!!, jwt!!)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        tvNombre = view.findViewById(R.id.tvNombre)
        tvEmail = view.findViewById(R.id.tvEmail)
        ivFotoPerfil = view.findViewById(R.id.ivFotoPerfil)
        btnCambiarFoto = view.findViewById(R.id.btnCambiarFoto)
        btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil)

        val prefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        jwt = prefs.getString("jwt", null)
        emailUsuario = jwt?.let { decodeEmailFromJWT(it) }

        btnCambiarFoto.setOnClickListener {
            seleccionarImagen.launch("image/*")
        }

        cargarDatosUsuario()

        btnEditarPerfil.setOnClickListener {
            findNavController().navigate(R.id.nav_editar_perfil)
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        cargarDatosUsuario() // 🔄 recarga al volver al fragmento
    }

    private fun cargarDatosUsuario() {
        if (jwt == null || emailUsuario.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Token no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        ApiUtils.apiService.obtenerUsuarioPorEmail(emailUsuario!!, "Bearer $jwt")
            .enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    if (response.isSuccessful && response.body() != null) {
                        val usuario = response.body()!!
                        Log.d("PERFIL", "URL de imagen recibida: ${usuario.fotoPerfil}")

                        tvNombre.text = usuario.nombre
                        tvEmail.text = usuario.email

                        Glide.with(requireContext())
                            .load(usuario.fotoPerfil)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                    } else {
                        Toast.makeText(requireContext(), "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun subirImagenAlServidor(uri: Uri, email: String, token: String) {
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val requestFile = inputStream?.readBytes()?.toRequestBody("image/*".toMediaTypeOrNull())

        if (requestFile != null) {
            val nombreArchivo = "foto_${System.currentTimeMillis()}.jpg"

            val body = MultipartBody.Part.createFormData(
                "foto", nombreArchivo, requestFile
            )

            ApiUtils.apiService.actualizarFotoPerfil(email, body, "Bearer $token")
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Foto actualizada en el servidor", Toast.LENGTH_SHORT).show()
                            cargarDatosUsuario() // 🔄 Recarga después de subir la imagen
                        } else {
                            Toast.makeText(requireContext(), "Error al subir imagen", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }
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
