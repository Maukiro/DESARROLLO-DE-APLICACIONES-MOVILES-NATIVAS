package com.example.examen1.activities

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.examen1.themes.ThemeManager
import com.example.examen1.R

class AsciiGameActivity : AppCompatActivity() {

    private lateinit var tvPregunta: TextView
    private lateinit var etRespuesta: EditText
    private lateinit var btnVerificar: Button
    private lateinit var btnSiguiente: Button

    private lateinit var mediaPlayer: MediaPlayer

    private var asciiCode = 0
    private val rango = 65..90 // Letras mayúsculas A-Z

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ascii_game)

        tvPregunta = findViewById(R.id.tv_ascii_pregunta)
        etRespuesta = findViewById(R.id.et_respuesta)
        btnVerificar = findViewById(R.id.btn_verificar)
        btnSiguiente = findViewById(R.id.btn_siguiente)

        // Inicializar el sonido
        mediaPlayer = MediaPlayer.create(this, R.raw.correct)

        generarNuevoCodigo()

        btnVerificar.setOnClickListener {
            val respuesta = etRespuesta.text.toString().uppercase()
            val letraCorrecta = asciiCode.toChar().toString()

            if (respuesta == letraCorrecta) {
                mediaPlayer.start() // 🎵 Reproducir sonido
                Toast.makeText(this, "¡Muy bien! 🎉", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Ups... era '$letraCorrecta'", Toast.LENGTH_SHORT).show()
            }
        }

        btnSiguiente.setOnClickListener {
            etRespuesta.text.clear()
            generarNuevoCodigo()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release() // Liberar recursos
    }

    private fun generarNuevoCodigo() {
        asciiCode = rango.random()
        tvPregunta.text = "¿Qué letra corresponde al código ASCII $asciiCode?"
    }
}
