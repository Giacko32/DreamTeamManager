package com.example.dreamteammanager.competizione

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.databinding.FragmentCreaCompetizioneBinding
import com.example.dreamteammanager.lega.LegheAdapter

class CreaCompetizioneFragment : Fragment() {
    lateinit var binding: FragmentCreaCompetizioneBinding

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

        val lista = ArrayList<Utente>()
        for(i in 1..50)
        {
            lista.add(Utente(username = "nome a caso", password = "password", id = 1, email = "email"))
        }
        val adapter = LegheAdapter(lista, true)
        binding.listaUserSelectable.layoutManager = LinearLayoutManager(context)
        binding.listaUserSelectable.adapter = adapter
    }


}