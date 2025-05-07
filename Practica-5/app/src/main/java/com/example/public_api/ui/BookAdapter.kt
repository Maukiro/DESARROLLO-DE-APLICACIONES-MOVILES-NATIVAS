import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.public_api.R
import com.example.public_api.models.Book
import com.example.public_api.models.FavoriteBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

class BookAdapter(private val books: List<Book>) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]

        holder.tvTitle.text = book.title
        holder.tvAuthor.text = book.author_name?.joinToString(", ") ?: "Desconocido"
        holder.tvYear.text = book.first_publish_year?.toString() ?: "Año no disponible"

        val coverUrl = "https://covers.openlibrary.org/b/id/${book.cover_i}-M.jpg"
        Glide.with(holder.itemView.context).load(coverUrl).into(holder.bookCover)

        holder.btnFavorite.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                Toast.makeText(holder.itemView.context, "Debes iniciar sesión", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = Firebase.firestore

            val generatedBookId = "${book.title}_${book.first_publish_year ?: 0}"

            val favorite = FavoriteBook(
                bookId = generatedBookId,
                title = book.title ?: "Sin título",
                author = book.author_name?.joinToString(", ") ?: "Desconocido",
                year = book.first_publish_year
            )

            db.collection("users").document(userId)
                .collection("favorites")
                .document(generatedBookId)
                .set(favorite)
                .addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "Agregado a favoritos", Toast.LENGTH_SHORT).show()
                    holder.btnFavorite.setImageResource(R.drawable.ic_favorite)
                }
                .addOnFailureListener {
                    Toast.makeText(holder.itemView.context, "Error al guardar", Toast.LENGTH_SHORT).show()
                }

        }
    }

    override fun getItemCount(): Int = books.size

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        val tvYear: TextView = itemView.findViewById(R.id.tvYear)
        val bookCover: ImageView = itemView.findViewById(R.id.bookCover)
        val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)
    }
}
