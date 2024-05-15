package com.example.dreamteammanager.lega

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.databinding.FragmentLegaviewBinding
import com.example.dreamteammanager.databinding.FragmentListaCompetizioniBinding


class LegaView : Fragment() {
    lateinit var binding: FragmentLegaviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.CompetizioniButton.setOnClickListener{
            (context as AppCompatActivity).supportFragmentManager.commit {
                setReorderingAllowed(true)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                replace<ListaCompetizioni>(R.id.legafragmentcontainer)
                addToBackStack("competizioni")
            }
        }

        val lista = ArrayList<Utente>()
        for(i in 1..50)
        {
            lista.add(Utente(username = "nome a caso", password = "password", id = 1, email = "email"))
        }
        val adapter = LegheAdapter(lista)
        binding.recview.layoutManager = LinearLayoutManager(context)
        binding.recview.adapter = adapter
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLegaviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}