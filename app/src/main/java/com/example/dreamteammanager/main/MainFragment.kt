package com.example.dreamteammanager.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Lega
import com.example.dreamteammanager.databinding.FragmentMainBinding
import com.example.dreamteammanager.lega.LegaActivity


class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.creaNuovaLegaButton.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<CreaLegaFragment>(R.id.fragmentContainerView)
                addToBackStack("creaLega")
            }
        }

        val lega= Lega(1,"LEGA",100,1)
        val listaleghe=ArrayList<Lega>()
        for(i in 1..50){
            listaleghe.add(lega)
        }
        val adapter= LegheAdapter(listaleghe)
        adapter.setonclick(object : LegheAdapter.SetOnClickListener{
            override fun onClick(position: Int, lega: Lega){
                val legaintent = Intent(requireActivity(), LegaActivity::class.java)
                legaintent.putExtra("lega",lega)
                startActivity(legaintent)
            }
        })
        binding.recyclerView.layoutManager=LinearLayoutManager(context)
        binding.recyclerView.adapter=adapter

        }

}