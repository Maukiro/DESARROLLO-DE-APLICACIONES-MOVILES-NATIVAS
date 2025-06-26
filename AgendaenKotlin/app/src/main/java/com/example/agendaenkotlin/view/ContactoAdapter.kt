package com.example.agendaenkotlin.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agendaenkotlin.R
import com.example.agendaenkotlin.model.Contacto

class ContactoAdapter(private val onItemClick: (Contacto) -> Unit) : ListAdapter<Contacto, ContactoAdapter.ContactosViewHolder>(ContactoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactosViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contactos, parent, false)
        return ContactosViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactosViewHolder, position: Int) {
        val contacto = getItem(position)
        holder.nombre.text = contacto.nombre
        holder.numero.text = contacto.numero

        holder.itemView.setOnClickListener {
            onItemClick(contacto)
        }
    }

    class ContactosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.nameUser)
        val numero: TextView = itemView.findViewById(R.id.numberUser)
    }

}