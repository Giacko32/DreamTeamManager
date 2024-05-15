package com.example.dreamteammanager.lega

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Competizione
import com.example.dreamteammanager.databinding.FragmentListaCompetizioniBinding

class ListaCompetizioni : Fragment() {

    lateinit var binding: FragmentListaCompetizioniBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lista = ArrayList<Competizione>(0)
        for(i in 1..20)
        {
            lista.add(Competizione(1,"suca","suca",3))
        }
        val adapter = CompetizioniAdapter(lista)

        binding.recviewcomp.adapter=adapter
        binding.recviewcomp.layoutManager=LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListaCompetizioniBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}