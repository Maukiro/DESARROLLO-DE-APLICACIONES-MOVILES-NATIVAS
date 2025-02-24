package com.example.homework2

import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnCalculator = findViewById<ImageButton>(R.id.imageButton);
        btnCalculator.setOnClickListener{
            val intent = Intent(this@MainActivity, CalculatorActivity::class.java)
            startActivity(intent)

        }

        val btnChronometer = findViewById<ImageButton>(R.id.imageButton2);
        btnChronometer.setOnClickListener{
            val intent = Intent(this@MainActivity, ChronometerActivity::class.java)
            startActivity(intent)

        }

    }
}