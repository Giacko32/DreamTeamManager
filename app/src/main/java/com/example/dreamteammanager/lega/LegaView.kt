package com.example.dreamteammanager.lega

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Competizione
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.databinding.FragmentLegaviewBinding


class LegaView : Fragment() {
    lateinit var binding: FragmentLegaviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listaCompetizioni = Dialog(requireActivity())

        binding.CompetizioniButton.setOnClickListener{
            listaCompetizioni.setContentView(R.layout.dialog_lista_competizioni)
            listaCompetizioni.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val rv = listaCompetizioni.findViewById<RecyclerView>(R.id.recviewcomp)
            val lista = ArrayList<Competizione>(0)
            for(i in 1..10)
            {
                lista.add(Competizione(0, "competizione a caso", "motogp", 0))
            }
            val adapter = CompetizioniAdapter(lista)
            rv.layoutManager = LinearLayoutManager(context)
            rv.adapter = adapter
            listaCompetizioni.show()
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