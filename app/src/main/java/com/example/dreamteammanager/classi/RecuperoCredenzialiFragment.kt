package com.example.dreamteammanager.classi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dreamteammanager.databinding.FragmentRecuperoCredenzialiBinding


class RecuperoCredenzialiFragment : Fragment() {
    lateinit var binding: FragmentRecuperoCredenzialiBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecuperoCredenzialiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ottieni un riferimento agli elementi della vista tramite il binding
        val emailEditText = binding.emailEditText
        val codeEditText = binding.codeEditText
        val actionButton = binding.actionButton

        // Mostra il campo del codice e cambia il testo del pulsante quando viene premuto
        actionButton.setOnClickListener {
            if (codeEditText.visibility == View.GONE) {
                codeEditText.visibility = View.VISIBLE
                emailEditText.visibility = View.GONE
                actionButton.text = "Conferma Codice"
            } else {
                // Logica per confermare il codice qui
            }
        }
    }


}