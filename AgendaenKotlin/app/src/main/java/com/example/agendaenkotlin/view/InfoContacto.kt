package com.example.agendaenkotlin.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.agendaenkotlin.databinding.ActivityInfoConatctoBinding
import com.example.agendaenkotlin.model.Contacto
import com.example.agendaenkotlin.viewmodel.AgendaViewModel

class InfoContacto : AppCompatActivity() {
    private lateinit var binding: ActivityInfoConatctoBinding
    private lateinit var viewModel: AgendaViewModel
    private val contacto = intent.getSerializableExtra("contacto") as? Contacto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView()
        initObjects()
        setInitValues()
    }

    private fun initObjects() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setInitValues() {
                binding.nameUser1.text = contacto?.nombre ?: "Nombre no disponible"
        binding.numberUser1.text = contacto?.numero ?: "Número no disponible"
    }

    private fun initContentView() {
        binding = ActivityInfoConatctoBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
        viewModel = ViewModelProvider(this)[AgendaViewModel::class.java]
    }

    private fun initListeners(){
        binding.buttonDelete.setOnClickListener {
            contacto?.let {
                viewModel.eliminarContacto(this, it.id)
            }
        }

        viewModel.contactoEliminado.observe(this) { eliminado ->
            if (eliminado) {
                Toast.makeText(this, "Contacto eliminado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al eliminar contacto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}