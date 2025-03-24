package com.example.examen1.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.examen1.R
import com.example.examen1.activities.AsciiGameActivity
import com.example.examen1.activities.BinarioActivity

class InicioFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el layout
        return inflater.inflate(R.layout.fragment_inicio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listeners para los botones
        view.findViewById<Button>(R.id.btn_binario).setOnClickListener {
            val intent = Intent(requireContext(), BinarioActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.btn_ascii).setOnClickListener {
            val intent = Intent(requireContext(), AsciiGameActivity::class.java)
            startActivity(intent)
        }
    }
}
