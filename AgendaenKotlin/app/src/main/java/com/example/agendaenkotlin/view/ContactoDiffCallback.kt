package com.example.agendaenkotlin.view

import androidx.recyclerview.widget.DiffUtil
import com.example.agendaenkotlin.model.Contacto

class ContactoDiffCallback : DiffUtil.ItemCallback<Contacto>() {
    override fun areItemsTheSame(oldItem: Contacto, newItem: Contacto): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Contacto, newItem: Contacto): Boolean {
        return oldItem == newItem
    }
}