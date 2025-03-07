package com.example.basicbackend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var textViewMensaje: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewMensaje = findViewById(R.id.textViewMensaje)

        obtenerMensaje()
    }

    private fun obtenerMensaje() {
        RetrofitClient.instance.obtenerMensaje().enqueue(object : Callback<Mensaje> {
            override fun onResponse(call: Call<Mensaje>, response: Response<Mensaje>) {
                if (response.isSuccessful) {
                    textViewMensaje.text = response.body()?.mensaje ?: "Respuesta vacía"
                } else {
                    Toast.makeText(applicationContext, "Error en la respuesta", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Mensaje>, t: Throwable) {
                Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
