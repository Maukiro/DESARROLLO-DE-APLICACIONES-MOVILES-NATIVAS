package com.example.public_api.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.public_api.R
import com.example.public_api.models.FavoriteBook

class FavoritesAdapter(private val books: List<FavoriteBook>) :
    RecyclerView.Adapter<FavoritesAdapter.FavViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val book = books[position]
        holder.title.text = book.title
        holder.author.text = book.author
        holder.year.text = book.year?.toString() ?: "Desconocido"
        Glide.with(holder.itemView.context)
            .load("https://covers.openlibrary.org/b/id/${book.bookId}-M.jpg")
            .placeholder(R.drawable.ic_book_placeholder)
            .into(holder.cover)
    }

    override fun getItemCount() = books.size

    class FavViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTitle)
        val author: TextView = view.findViewById(R.id.tvAuthor)
        val year: TextView = view.findViewById(R.id.tvYear)
        val cover: ImageView = view.findViewById(R.id.bookCover)
    }
}
