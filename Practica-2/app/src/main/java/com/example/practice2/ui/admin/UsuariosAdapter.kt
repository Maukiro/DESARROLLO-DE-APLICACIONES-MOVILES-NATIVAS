package com.example.practice2.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practice2.R
import com.example.practice2.model.Usuario

class UsuariosAdapter(
    private val usuarios: List<Usuario>,
    private val onEditarClick: (Usuario) -> Unit,
    private val onEliminarClick: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.bind(usuario)
    }

    override fun getItemCount(): Int = usuarios.size

    inner class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombreUsuario)
        private val tvEmail: TextView = itemView.findViewById(R.id.tvEmailUsuario)
        private val ivFoto: ImageView = itemView.findViewById(R.id.ivFotoUsuario)
        private val btnEditar: ImageView = itemView.findViewById(R.id.btnEditar)
        private val btnEliminar: ImageView = itemView.findViewById(R.id.btnEliminar)

        fun bind(usuario: Usuario) {
            tvNombre.text = usuario.nombre
            tvEmail.text = usuario.email

            Glide.with(itemView.context)
                .load(usuario.fotoPerfil)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(ivFoto)

            btnEditar.setOnClickListener {
                onEditarClick(usuario)
            }

            btnEliminar.setOnClickListener {
                onEliminarClick(usuario)
            }
        }
    }
}