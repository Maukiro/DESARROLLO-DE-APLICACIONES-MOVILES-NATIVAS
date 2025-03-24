package com.example.examen1.activities

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.examen1.R
import com.example.examen1.themes.ThemeManager

class BinarioActivity : AppCompatActivity() {

    private lateinit var switches: List<Switch>
    private lateinit var tvDecimal: TextView
    private lateinit var mediaPlayer: MediaPlayer

    private var numeroDecimal = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binario)

        tvDecimal = findViewById(R.id.tv_decimal)

        mediaPlayer = MediaPlayer.create(this, R.raw.correct)

        switches = listOf(
            findViewById(R.id.sw_bit3), // 8
            findViewById(R.id.sw_bit2), // 4
            findViewById(R.id.sw_bit1), // 2
            findViewById(R.id.sw_bit0)  // 1
        )

        generarNumero()

        switches.forEach { sw ->
            sw.setOnCheckedChangeListener { _, _ ->
                verificarResultado()
            }
        }
    }

    private fun generarNumero() {
        numeroDecimal = (0..15).random()
        tvDecimal.text = "Convierte este número a binario: $numeroDecimal"
    }

    private fun verificarResultado() {
        val resultadoBinario = switches.mapIndexed { _, sw ->
            if (sw.isChecked) 1 else 0
        }

        val valorDecimal = resultadoBinario[0] * 8 +
                resultadoBinario[1] * 4 +
                resultadoBinario[2] * 2 +
                resultadoBinario[3] * 1

        if (valorDecimal == numeroDecimal) {
            mediaPlayer.start() // 🔊 Reproducir sonido
            Toast.makeText(this, "¡Correcto! 🎉", Toast.LENGTH_SHORT).show()
            generarNumero()
            switches.forEach { it.isChecked = false }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release() // Liberar recursos
    }
}
