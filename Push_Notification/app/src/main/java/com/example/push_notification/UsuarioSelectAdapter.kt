package com.example.push_notification

import Usuario
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsuarioSelectAdapter(
    private val lista: List<Usuario>,
    private val selectedUids: MutableSet<String>
) : RecyclerView.Adapter<UsuarioSelectAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cbUsuario: CheckBox = view.findViewById(R.id.cbUsuario)
        val tvEmail: TextView = view.findViewById(R.id.tvEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario_select, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = lista[position]
        holder.tvEmail.text = "${usuario.nombre} (${usuario.email})"
        holder.cbUsuario.setOnCheckedChangeListener(null)
        holder.cbUsuario.isChecked = selectedUids.contains(usuario.uid)
        holder.cbUsuario.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedUids.add(usuario.uid) else selectedUids.remove(usuario.uid)
        }
    }

    override fun getItemCount(): Int = lista.size
}
