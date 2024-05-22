package com.example.dreamteammanager.lega

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
import com.example.dreamteammanager.competizione.CompetizioneActivity
import com.example.dreamteammanager.competizione.CompetizioniAdapter
import com.example.dreamteammanager.competizione.CreaCompetizioneFragment
import com.example.dreamteammanager.databinding.FragmentLegaviewBinding
import com.example.dreamteammanager.main.LegheAdapter
import com.example.dreamteammanager.viewmodel.ImagesVM
import com.example.dreamteammanager.viewmodel.SharedPreferencesManager
import com.example.dreamteammanager.viewmodel.SingleLegaVM
import com.example.dreamteammanager.viewmodel.parseJsonToLega
import com.example.dreamteammanager.viewmodel.parseJsonToModel
import com.google.gson.Gson


class LegaView : Fragment() {
    lateinit var binding: FragmentLegaviewBinding
    private val singleLegaVM: SingleLegaVM by activityViewModels()
    private val imagesVM: ImagesVM by viewModels()
    lateinit var utente:Utente
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        utente= parseJsonToModel(SharedPreferencesManager.getString("utente",""))
        if(utente.id!=singleLegaVM.lega.value!!.id_amministratore)
        {
            binding.invitaButton.visibility = View.GONE
            binding.CreaCompetizioneButton.visibility = View.GONE
            binding.VisualizzaRichiesteButton.visibility = View.GONE
        }

        binding.nomelega.text = singleLegaVM.lega.value!!.name
        singleLegaVM.getPartecipanti()
        imagesVM.getLegaImage(requireContext(), singleLegaVM.lega.value!!.image, binding.imageView)
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
            adapter.setonclick(object : CompetizioniAdapter.SetOnClickListener {
                override fun onClick(position: Int, competizione: Competizione) {
                    val competizioneintent = Intent(requireActivity(), CompetizioneActivity::class.java)
                    startActivity(competizioneintent)
                }
            })
            rv.layoutManager = LinearLayoutManager(context)
            rv.adapter = adapter
            listaCompetizioni.show()
        }

        val listaInvitaUtente = Dialog(requireActivity())
        binding.invitaButton.setOnClickListener{
            singleLegaVM.getutentiinivito()

        }
        singleLegaVM.utentiinvito.observe(viewLifecycleOwner){
            if(it==true){
                listaInvitaUtente.setContentView(R.layout.dialog_invita_giocatore)
                listaInvitaUtente.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                listaInvitaUtente.findViewById<ImageView>(R.id.search_icon)
                val rvInvita = listaInvitaUtente.findViewById<RecyclerView>(R.id.recycler_view_invita)
                val adapter = InvitaGiocatoriAdapter(singleLegaVM.invitati.value!!, imagesVM)
                rvInvita.layoutManager = LinearLayoutManager(context)
                rvInvita.adapter = adapter
                singleLegaVM.resetutentiinvito()
                listaInvitaUtente.show()
                binding.progressBar.visibility=View.GONE
            }else if(it==false){
                binding.progressBar.visibility=View.VISIBLE
            }
        }


        singleLegaVM.scaricando.observe(viewLifecycleOwner){
            if(it==true){
                binding.progressBar.visibility = View.VISIBLE
            }
            else if(it==false){
                val adapter = PartecipantiAdapter(singleLegaVM.partecipanti.value!!, false, imagesVM, singleLegaVM.lega.value!!.id_amministratore)
                binding.recview.layoutManager = LinearLayoutManager(context)
                binding.recview.adapter = adapter
                binding.progressBar.visibility = View.GONE
            }
        }



        binding.CreaCompetizioneButton.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<CreaCompetizioneFragment>(R.id.legafragmentcontainer)
                addToBackStack("CreaComp")
            }
        }
        binding.VisualizzaRichiesteButton.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<RichiesteFragment>(R.id.legafragmentcontainer)
                addToBackStack("Richieste")
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