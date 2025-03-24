package com.example.examen1.fragments

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import com.example.examen1.R

class TemasFragment : Fragment() {

    private lateinit var radioGuinda: RadioButton
    private lateinit var radioAzul: RadioButton
    private lateinit var btnAplicar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_temas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioGuinda = view.findViewById(R.id.radio_guinda)
        radioAzul = view.findViewById(R.id.radio_azul)
        btnAplicar = view.findViewById(R.id.btn_aplicar)

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val temaActual = prefs.getString("app_theme", "guinda")

        // Preseleccionar el tema guardado
        if (temaActual == "guinda") {
            radioGuinda.isChecked = true
        } else {
            radioAzul.isChecked = true
        }

        btnAplicar.setOnClickListener {
            val nuevoTema = if (radioGuinda.isChecked) "guinda" else "azul"
            prefs.edit().putString("app_theme", nuevoTema).apply()

            // Reiniciar actividad para aplicar el nuevo tema
            activity?.recreate()
        }
    }
}
