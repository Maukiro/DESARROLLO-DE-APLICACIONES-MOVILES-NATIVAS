package com.example.filemanagerapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filemanagerapp.adapter.FileAdapter
import com.example.filemanagerapp.model.FileItem
import com.example.filemanagerapp.model.TextViewerActivity
import java.io.File
import android.util.Log
import android.net.Uri
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.filemanagerapp.model.ImageViewerActivity
import com.example.filemanagerapp.model.ThemeHelper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar



class MainActivity : AppCompatActivity() {

    private lateinit var textCurrentPath: TextView
    private lateinit var recyclerView: RecyclerView
    private var currentPath: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

    // Para pedir permisos (solo si API < 30 o si no usarás MANAGE_EXTERNAL_STORAGE)
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            loadFiles(currentPath)
        } else {
            Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeHelper.applyTheme(this) // 👈 PRIMERO
        super.onCreate(savedInstanceState) // 👈 SEGUNDO
        setContentView(R.layout.activity_main) // 👈 TERCERO

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        textCurrentPath = findViewById(R.id.textCurrentPath)
        recyclerView = findViewById(R.id.recyclerViewFiles)
        recyclerView.layoutManager = LinearLayoutManager(this)
        currentPath?.let { loadFiles(it) }

        checkPermissions()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 o superior → Necesitamos permiso MANAGE_EXTERNAL_STORAGE
            if (Environment.isExternalStorageManager()) {
                // Ya tenemos permiso, cargar archivos
                currentPath?.let { loadFiles(it) }
            } else {
                // No tenemos permiso, solicitarlo abriendo configuración
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.data = Uri.parse("package:$packageName")
                    startActivity(intent)
                } catch (e: Exception) {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    startActivity(intent)
                }
                Toast.makeText(this, "Debes otorgar permisos de acceso a archivos", Toast.LENGTH_LONG).show()
            }
        } else {
            // Android 10 o menor → Solicitar permiso READ_EXTERNAL_STORAGE normal
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                currentPath?.let { loadFiles(it) }
            }
        }
    }

    override fun onBackPressed() {
        val parent = currentPath.parentFile
        if (parent != null && parent.canRead()) {
            loadFiles(parent) // Retrocede a la carpeta anterior
        } else {
            super.onBackPressed() // Si ya estás en la raíz, salir normalmente
        }
    }

    private fun loadFiles(path: File) {
        if (!path.exists() || !path.isDirectory) {
            Toast.makeText(this, "Ruta inválida o inaccesible", Toast.LENGTH_SHORT).show()
            return
        }

        currentPath = path
        textCurrentPath.text = currentPath.absolutePath // Mostrar ruta actual

        val files = path.listFiles()?.filter {
            !it.name.startsWith(".") // Ignorar ocultos
        }

        files?.forEach {
            Log.d("FileManager", "Encontrado: ${it.name} - ¿Es directorio?: ${it.isDirectory}")
        }


        if (files.isNullOrEmpty()) {
            Toast.makeText(this, "Esta carpeta está vacía", Toast.LENGTH_SHORT).show()
        }

        val fileItems = files?.map {
            FileItem(it.name, it.absolutePath, it.isDirectory)
        }?.sortedWith(compareByDescending<FileItem> { it.isDirectory }.thenBy { it.name })
            ?: emptyList()

        val adapter = FileAdapter(fileItems) { clickedItem ->
            val clickedFile = File(clickedItem.path)
            if (clickedFile.isDirectory) {
                loadFiles(clickedFile)
            } else {
                when (clickedFile.extension.lowercase()) {
                    "txt", "md" -> openTextFile(clickedFile)
                    "jpg", "jpeg", "png", "gif" -> openImageFile(clickedFile)
                    else -> openWithOtherApp(clickedFile)
                }
            }
        }

        recyclerView.adapter = adapter
    }


    private fun openTextFile(file: File) {
        val intent = TextViewerActivity.newIntent(this, file.absolutePath)
        startActivity(intent)
    }

    private fun openImageFile(file: File) {
        val intent = ImageViewerActivity.newIntent(this, file.absolutePath)
        startActivity(intent)
    }

    private fun openWithOtherApp(file: File) {
        val uri = FileProvider.getUriForFile(this, "${packageName}.provider", file)
        val mimeType = contentResolver.getType(uri)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mimeType ?: "*/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "No hay aplicación disponible para abrir este archivo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showThemeSelector() {
        val options = arrayOf("Tema Guinda (IPN)", "Tema Azul (ESCOM)")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona un Tema")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> {
                    ThemeHelper.saveTheme(this, ThemeHelper.THEME_GUINDA)
                    recreate() // Reiniciar actividad para aplicar el tema
                }
                1 -> {
                    ThemeHelper.saveTheme(this, ThemeHelper.THEME_AZUL)
                    recreate() // Reiniciar actividad para aplicar el tema
                }
            }
        }
        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_change_theme -> {
                showThemeSelector() // 👈 Mostrar selección de temas
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }





}
