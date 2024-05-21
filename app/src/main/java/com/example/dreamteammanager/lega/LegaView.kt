package com.example.dreamteammanager.lega

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Competizione
import com.example.dreamteammanager.classi.Lega
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.competizione.CompetizioniAdapter
import com.example.dreamteammanager.competizione.CreaCompetizioneFragment
import com.example.dreamteammanager.databinding.FragmentLegaviewBinding
import com.example.dreamteammanager.viewmodel.SharedPreferencesManager
import com.example.dreamteammanager.viewmodel.SingleLegaVM
import com.example.dreamteammanager.viewmodel.parseJsonToLega
import com.google.gson.Gson


class LegaView : Fragment() {
    lateinit var binding: FragmentLegaviewBinding
    private val singleLegaVM: SingleLegaVM by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nomelega.text = singleLegaVM.lega.value!!.name
        singleLegaVM.getPartecipanti()
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

        val listaInvitaUtente = Dialog(requireActivity())
        binding.invitaButton.setOnClickListener{
            listaInvitaUtente.setContentView(R.layout.dialog_invita_giocatore)
            listaInvitaUtente.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val rvInvita = listaInvitaUtente.findViewById<RecyclerView>(R.id.recycler_view_invita)
            val listaGiocatori = ArrayList<Utente>()
            for(i in 1..50)
            {
                listaGiocatori.add(Utente(username = "Username", password = "password", id = 1, email = "email"))
            }
            val adapter = InvitaGiocatoriAdapter(listaGiocatori)
            rvInvita.layoutManager = LinearLayoutManager(context)
            rvInvita.adapter = adapter
            listaInvitaUtente.show()
        }


        singleLegaVM.scaricando.observe(viewLifecycleOwner){
            if(it==true){
                binding.progressBar.visibility = View.VISIBLE
            }
            else if(it==false){
                val adapter = PartecipantiAdapter(singleLegaVM.partecipanti.value!!, false)
                binding.recview.layoutManager = LinearLayoutManager(context)
                binding.recview.adapter = adapter
                binding.progressBar.visibility = View.INVISIBLE
            }
        }



        binding.CreaCompetizioneButton.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<CreaCompetizioneFragment>(R.id.legafragmentcontainer)
                addToBackStack("CreaComp")
            }
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLegaviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}