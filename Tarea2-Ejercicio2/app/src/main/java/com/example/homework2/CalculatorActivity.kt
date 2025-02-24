package com.example.homework2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CalculatorActivity : AppCompatActivity() {

    private lateinit var txtResultado: TextView
    private var currentInput = ""
    private var operador = ""
    private var firstValue = 0.0
    private var isNewOp = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        txtResultado = findViewById(R.id.txtResultado)
    }

    fun onDigitClick(view: View) {
        if (isNewOp) {
            txtResultado.text = ""
            isNewOp = false
        }

        val button = view as Button
        currentInput += button.text.toString()
        txtResultado.text = currentInput
    }

    fun onOperatorClick(view: View) {
        val button = view as Button
        operador = button.text.toString()
        firstValue = currentInput.toDoubleOrNull() ?: 0.0
        currentInput = ""
        isNewOp = true
    }

    fun onEqualClick(view: View) {
        val secondValue = currentInput.toDoubleOrNull() ?: 0.0
        var resultado = 0.0

        when (operador) {
            "+" -> resultado = firstValue + secondValue
            "−" -> resultado = firstValue - secondValue
            "×" -> resultado = firstValue * secondValue
            "÷" -> resultado = if (secondValue != 0.0) firstValue / secondValue else 0.0
        }

        txtResultado.text = resultado.toString()
        currentInput = resultado.toString()
        isNewOp = true
    }

    fun onClearClick(view: View) {
        txtResultado.text = "0"
        currentInput = ""
        firstValue = 0.0
        operador = ""
        isNewOp = false
        }

    fun onBackClick(view: View) {
        finish() // Cierra la actividad actual y vuelve a la anterior
    }

}