package com.example.contador_de_toques


import android.os.Bundle
import android.os.SystemClock
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private var score = 0
    private var level = 1
    private var startTime = 0L
    private var currentFormat = "json"

    private lateinit var scoreText: TextView
    private lateinit var levelText: TextView
    private lateinit var timeText: TextView
    private lateinit var tapButton: Button
    private lateinit var saveButton: Button
    private lateinit var loadButton: Button
    private lateinit var formatSpinner: Spinner
    private lateinit var timer: Chronometer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scoreText = findViewById(R.id.scoreText)
        levelText = findViewById(R.id.levelText)
        timeText = findViewById(R.id.timeText)
        tapButton = findViewById(R.id.tapButton)
        saveButton = findViewById(R.id.saveButton)
        loadButton = findViewById(R.id.loadButton)
        formatSpinner = findViewById(R.id.formatSpinner)
        timer = findViewById(R.id.timer)

        timer.base = SystemClock.elapsedRealtime()
        timer.start()
        startTime = SystemClock.elapsedRealtime()

        setupSpinner()

        tapButton.setOnClickListener {
            score++
            if (score % 10 == 0) level++
            updateUI()
        }

        saveButton.setOnClickListener {
            val elapsedTime = SystemClock.elapsedRealtime() - startTime
            val state = GameState(score, level, elapsedTime, "guinda", currentFormat)
            FileManager.saveGame(this, state, currentFormat)
            Toast.makeText(this, "Partida guardada", Toast.LENGTH_SHORT).show()
        }

        loadButton.setOnClickListener {
            val state = FileManager.loadGame(this, currentFormat)
            score = state.score
            level = state.level
            startTime = SystemClock.elapsedRealtime() - state.timeElapsed
            timer.base = startTime
            updateUI()
            Toast.makeText(this, "Partida cargada", Toast.LENGTH_SHORT).show()
        }

        updateUI()
    }

    private fun setupSpinner() {
        val formats = arrayOf("json", "txt", "xml")
        formatSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, formats)
        formatSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long
            ) {
                currentFormat = formats[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateUI() {
        scoreText.text = "Puntos: $score"
        levelText.text = "Nivel: $level"
        val elapsed = (SystemClock.elapsedRealtime() - timer.base) / 1000
        timeText.text = "Tiempo: ${elapsed}s"
    }
}