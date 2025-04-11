package com.example.filemanagerapp.model

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.filemanagerapp.R
import com.jsibbold.zoomage.ZoomageView
import java.io.File


class ImageViewerActivity : AppCompatActivity() {

    private lateinit var zoomageView: ZoomageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        zoomageView = findViewById(R.id.zoomageView)

        val path = intent.getStringExtra(EXTRA_FILE_PATH)
        if (path != null) {
            val file = File(path)
            if (file.exists()) {
                zoomageView.setImageURI(file.toURI().toString().toUri())
            }
        }
    }

    companion object {
        private const val EXTRA_FILE_PATH = "extra_file_path"

        fun newIntent(context: Context, filePath: String): Intent {
            val intent = Intent(context, ImageViewerActivity::class.java)
            intent.putExtra(EXTRA_FILE_PATH, filePath)
            return intent
        }
    }
}
