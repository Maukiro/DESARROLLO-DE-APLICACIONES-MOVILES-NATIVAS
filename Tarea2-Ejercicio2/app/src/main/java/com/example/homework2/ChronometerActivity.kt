package com.example.homework2

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class ChronometerActivity : AppCompatActivity() {

    private lateinit var txtCronometro: TextView
    private lateinit var btnIniciar: Button
    private lateinit var btnPausar: Button
    private lateinit var btnReiniciar: Button

    private var handler = Handler()
    private var startTime: Long = 0L
    private var timeInMillis: Long = 0L
    private var timeSwap: Long = 0L
    private var updatedTime: Long = 0L
    private var isRunning: Boolean = false

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            timeInMillis = SystemClock.uptimeMillis() - startTime
            updatedTime = timeSwap + timeInMillis

            val secs = (updatedTime / 1000).toInt() % 60
            val mins = (updatedTime / 1000 / 60).toInt() % 60
            val hrs = (updatedTime / 1000 / 3600).toInt()

            txtCronometro.text = String.format("%02d:%02d:%02d", hrs, mins, secs)
            handler.postDelayed(this, 50)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chronometer)

        txtCronometro = findViewById(R.id.txtCronometro)
        btnIniciar = findViewById(R.id.btnIniciar)
        btnPausar = findViewById(R.id.btnPausar)
        btnReiniciar = findViewById(R.id.btnReiniciar)

        btnIniciar.setOnClickListener {
            if (!isRunning) {
                startTime = SystemClock.uptimeMillis()
                handler.postDelayed(runnable, 0)
                isRunning = true
            }
        }

        btnPausar.setOnClickListener {
            if (isRunning) {
                timeSwap += timeInMillis
                handler.removeCallbacks(runnable)
                isRunning = false
            }
        }

        btnReiniciar.setOnClickListener {
            startTime = 0L
            timeInMillis = 0L
            timeSwap = 0L
            updatedTime = 0L
            txtCronometro.text = "00:00:00"
            handler.removeCallbacks(runnable)
            isRunning = false
        }
    }
    fun onBackClick(view: View) {
        finish() // Cierra la actividad actual y vuelve a la anterior
    }

    override fun onPause() {
        super.onPause()
        if (isRunning) {
            timeSwap += timeInMillis
            handler.removeCallbacks(runnable) // Detiene el cronómetro temporalmente
            isRunning = false
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isRunning) {
            startTime = SystemClock.uptimeMillis()
            handler.postDelayed(runnable, 0) // Reanuda el cronómetro
            isRunning = true
        }
    }
}
