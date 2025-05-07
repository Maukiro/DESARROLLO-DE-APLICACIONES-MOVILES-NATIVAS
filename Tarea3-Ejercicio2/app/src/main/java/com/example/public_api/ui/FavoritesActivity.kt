package com.example.public_api.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.public_api.R
import com.example.public_api.models.FavoriteBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val books = mutableListOf<FavoriteBook>()
    private lateinit var adapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        recyclerView = findViewById(R.id.recyclerFavorites)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FavoritesAdapter(books)
        recyclerView.adapter = adapter

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        Firebase.firestore.collection("users").document(uid)
            .collection("favorites")
            .get()
            .addOnSuccessListener { result ->
                books.clear()
                for (doc in result) {
                    val book = doc.toObject(FavoriteBook::class.java)
                    books.add(book)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar favoritos", Toast.LENGTH_SHORT).show()
            }
    }
}
