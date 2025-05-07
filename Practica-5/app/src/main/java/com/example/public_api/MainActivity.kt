package com.example.public_api.ui

import BookAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.public_api.R
import com.example.public_api.models.Book
import com.example.public_api.network.RetrofitClient
import com.example.public_api.network.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class MainActivity : AppCompatActivity() {
    private lateinit var searchButton: Button
    private lateinit var searchInput: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton = findViewById(R.id.btnSearch)
        searchInput = findViewById(R.id.etSearch)
        recyclerView = findViewById(R.id.recyclerView)

        // Configuración de RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        bookAdapter = BookAdapter(emptyList())
        recyclerView.adapter = bookAdapter

        searchButton.setOnClickListener {
            val query = searchInput.text.toString()
            if (query.isNotEmpty()) {
                searchBooks(query)
            } else {
                Toast.makeText(this, "Ingrese un título", Toast.LENGTH_SHORT).show()
            }
        }

        val logoutButton = findViewById<Button>(R.id.btnLogout)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            GoogleSignIn.getClient(
                this,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            ).signOut().addOnCompleteListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val btnFavoritos = findViewById<Button>(R.id.btnVerFavoritos)
        btnFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }


    }



    private fun searchBooks(query: String) {
        RetrofitClient.instance.searchBooks(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.isSuccessful) {
                    val books = response.body()?.docs ?: emptyList()
                    displayBooks(books)
                } else {
                    Toast.makeText(applicationContext, "Error en la respuesta", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun displayBooks(books: List<Book>) {
        bookAdapter = BookAdapter(books)
        recyclerView.adapter = bookAdapter
    }

}
