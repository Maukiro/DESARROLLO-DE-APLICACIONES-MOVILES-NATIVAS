package com.example.filemanagerapp.model

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.filemanagerapp.R
import java.io.File

class TextViewerActivity : AppCompatActivity() {

    private lateinit var textViewContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_viewer)

        textViewContent = findViewById(R.id.textViewContent)

        val path = intent.getStringExtra(EXTRA_FILE_PATH)
        if (path != null) {
            val file = File(path)
            if (file.exists()) {
                val content = file.readText()
                textViewContent.text = content
            } else {
                textViewContent.text = "Error: archivo no encontrado."
            }
        } else {
            textViewContent.text = "Error al abrir archivo."
        }
    }

    companion object {
        private const val EXTRA_FILE_PATH = "extra_file_path"

        fun newIntent(context: Context, filePath: String): Intent {
            val intent = Intent(context, TextViewerActivity::class.java)
            intent.putExtra(EXTRA_FILE_PATH, filePath)
            return intent
        }
    }
}