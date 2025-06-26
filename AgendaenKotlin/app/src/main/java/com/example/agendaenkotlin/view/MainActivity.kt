package com.example.agendaenkotlin.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agendaenkotlin.R
import com.example.agendaenkotlin.databinding.ActivityMainBinding
import com.example.agendaenkotlin.viewmodel.AgendaViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel : AgendaViewModel

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: ContactoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView()
        initObjects()
        setInitValues()
        initObserver()
        initListeners()
    }

    private fun initContentView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        viewModel = ViewModelProvider(this)[AgendaViewModel::class.java]
    }

    private fun initObjects() {
        adapter = ContactoAdapter { contacto ->
            val intent = Intent(this, InfoContacto::class.java)
            intent.putExtra("contacto", contacto)
            startActivity(intent)
        }
    }

    private fun setInitValues() {
        binding.rvContactos.adapter = adapter
        binding.rvContactos.layoutManager = LinearLayoutManager(this)
    }

    private fun initObserver() {
        viewModel.getContactos().observe(this, adapter::submitList)
    }

    private fun initListeners() {
        binding.btnNuevo.setOnClickListener {
            viewModel.setNombreListo(binding.edtxNombre.getText().toString().isNotEmpty())
            viewModel.setNumeroListo(binding.edtxNumero.getText().toString().isNotEmpty())
            if (!viewModel.getNameListo()) {
                Toast.makeText(this, resources.getString(R.string.notName), Toast.LENGTH_SHORT).show()
            } else if (!viewModel.getNumberListo()) {
                Toast.makeText(this, resources.getString(R.string.notNumber), Toast.LENGTH_SHORT).show()
            } else if (!viewModel.verificarNum(binding.edtxNumero.getText().toString())) {
                Toast.makeText(this, resources.getString(R.string.numInvalid), Toast.LENGTH_SHORT).show()
            } else if (viewModel.getNameListo() && viewModel.getNumberListo() && viewModel.verificarNum(binding.edtxNumero.getText().toString())) {
                viewModel.setName(binding.edtxNombre.getText().toString())
                viewModel.setNumber(binding.edtxNumero.getText().toString())
                viewModel.insertar(this)
                viewModel.obtener(this)
                Toast.makeText(this, resources.getString(R.string.ok), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.obtener(this)
    }
}