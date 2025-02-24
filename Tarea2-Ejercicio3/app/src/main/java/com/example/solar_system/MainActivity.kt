package com.example.solar_system

import android.content.Intent
import android.os.Bundle
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
        val btnSun = findViewById<ImageButton>(R.id.imageButton);
        btnSun.setOnClickListener{
            val intent = Intent(this@MainActivity, SunActivity::class.java)
            startActivity(intent)

        }
        val btnMercury = findViewById<ImageButton>(R.id.imageButton6);
        btnMercury.setOnClickListener{
            val intent = Intent(this@MainActivity, MercuryActivity::class.java)
            startActivity(intent)

        }
        val btnVenus = findViewById<ImageButton>(R.id.imageButton7);
        btnVenus.setOnClickListener{
            val intent = Intent(this@MainActivity, VenusActivity::class.java)
            startActivity(intent)

        }
        val btnEarth= findViewById<ImageButton>(R.id.imageButton8);
        btnEarth.setOnClickListener{
            val intent = Intent(this@MainActivity, EarthActivity::class.java)
            startActivity(intent)

        }
        val btnMars = findViewById<ImageButton>(R.id.imageButton9);
        btnMars.setOnClickListener{
            val intent = Intent(this@MainActivity, MarsActivity::class.java)
            startActivity(intent)

        }
        val btnJupiter = findViewById<ImageButton>(R.id.imageButton10);
        btnJupiter.setOnClickListener{
            val intent = Intent(this@MainActivity, JupiterActivity::class.java)
            startActivity(intent)

        }



    }
}