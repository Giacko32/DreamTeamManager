package com.example.dreamteammanager

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

        binding.actionButton.setOnClickListener {

            if (binding.codicefield.visibility == View.GONE) {
                binding.codicefield.visibility = View.VISIBLE
                binding.mailfield.visibility = View.GONE
                binding.actionButton.text = "Conferma Codice"
            } else {
                // Logica per confermare il codice qui
            }
        }
    }


}