package com.example.agendaenkotlin.viewmodel

import android.content.Context
import android.content.LocusId
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agendaenkotlin.model.Contacto
import com.example.agendaenkotlin.repository.ContactosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AgendaViewModel : ViewModel() {
    private val _contactos: MutableLiveData<ArrayList<Contacto>> = MutableLiveData<ArrayList<Contacto>>()
    private val contacto: LiveData<ArrayList<Contacto>> = _contactos
    private val _contactoEliminado = MutableLiveData<Boolean>()
    val contactoEliminado: LiveData<Boolean> = _contactoEliminado



    private lateinit var nombre : String
    private lateinit var numero : String
    private var nombreListo = false
    private var numeroListo = false

    fun getContactos(): LiveData<ArrayList<Contacto>> {
        return contacto
    }

    fun insertar(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            ContactosRepository.insertarContacto(nombre, numero, context)
        }
    }

    fun obtener(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            ContactosRepository.obtenerContactos(context)
            _contactos.postValue(ContactosRepository.obtenerContactos(context))
        }
    }


    fun eliminarContacto(context: Context, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val resultado = ContactosRepository.eliminarContacto(context, id)
            if (resultado){
                val nuevaLista = ContactosRepository.obtenerContactos(context)
                _contactos.postValue(nuevaLista)
            }
            _contactoEliminado.postValue(resultado)
        }
    }

    fun verificarNum(numero : String): Boolean {
        return Patterns.PHONE.matcher(numero).matches()
    }

    fun setName(nombre: String) {
        this.nombre = nombre
    }
    fun setNumber(numero: String) {
        this.numero = numero
    }

    fun getNameListo(): Boolean {
        return nombreListo
    }
    fun getNumberListo(): Boolean {
        return numeroListo
    }

    fun setNombreListo(nombreListo: Boolean) {
        this.nombreListo = nombreListo
    }
    fun setNumeroListo(numeroListo: Boolean) {
        this.numeroListo = numeroListo
    }
}