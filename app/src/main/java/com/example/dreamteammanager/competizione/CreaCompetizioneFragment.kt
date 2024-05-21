package com.example.dreamteammanager.competizione

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Competizione
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.databinding.FragmentCreaCompetizioneBinding
import com.example.dreamteammanager.lega.PartecipantiAdapter
import com.example.dreamteammanager.viewmodel.CompetizioniVM

class CreaCompetizioneFragment : Fragment() {
    lateinit var binding: FragmentCreaCompetizioneBinding
    val compViewModel: CompetizioniVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreaCompetizioneBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compViewModel.setCompetizione(Competizione(0, "", "Serie A", 0))
        compViewModel.setSport("Serie A")

        val lista = ArrayList<Utente>()
        for (i in 1..50) {
            lista.add(
                Utente(
                    username = "nome a caso",
                    password = "password",
                    id = 1,
                    email = "email"
                )
            )
        }
        val adapter = PartecipantiAdapter(lista, true)
        binding.listaUserSelectable.layoutManager = LinearLayoutManager(context)
        binding.listaUserSelectable.adapter = adapter

        binding.sportgroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                val sport = group.findViewById<RadioButton>(checkedId).text.toString()
                if (compViewModel.competizione.value != null) {
                    compViewModel.setSport(sport)
                }
            }
        }

        compViewModel.sport.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    "Serie A" -> binding.immagine.setImageResource(R.drawable.seriealogo)
                    "Formula Uno" -> binding.immagine.setImageResource(R.drawable.formula_1)
                    "MotoGP" -> binding.immagine.setImageResource(R.drawable.motogplogo)
                    else -> binding.immagine.setImageResource(R.drawable.seriealogo)
                }
            } else {
                binding.immagine.setImageResource(R.drawable.seriealogo)
            }
        }
    }


}