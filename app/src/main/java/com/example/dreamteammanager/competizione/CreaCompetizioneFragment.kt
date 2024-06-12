package com.example.dreamteammanager.competizione

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Competizione
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.databinding.FragmentCreaCompetizioneBinding
import com.example.dreamteammanager.lega.LegaView
import com.example.dreamteammanager.lega.PartecipantiAdapter
import com.example.dreamteammanager.viewmodel.CompetizioniVM
import com.example.dreamteammanager.viewmodel.ImagesVM
import com.example.dreamteammanager.viewmodel.SingleLegaVM

class CreaCompetizioneFragment : Fragment() {
    lateinit var binding: FragmentCreaCompetizioneBinding
    private val compViewModel: CompetizioniVM by viewModels()
    private val singleLegaVM: SingleLegaVM by activityViewModels()
    private val imagesVM: ImagesVM by viewModels()

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
        binding.sportgroup.check(R.id.serieAbox)
        val adapter = PartecipantiAdapter(singleLegaVM.partecipanti.value!!, true, imagesVM, null)
        adapter.setonItemCheckListener(object : PartecipantiAdapter.OnItemCheckListener {
            override fun onItemCheck(utente: Utente) {
                singleLegaVM.listapartcompiti.value!!.add(utente)
            }

            override fun onItemUncheck(utente: Utente) {
                singleLegaVM.listapartcompiti.value!!.remove(utente)
            }
        })
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

        binding.CreaButton.setOnClickListener {
            if (compViewModel.competizione.value != null) {
                if (singleLegaVM.listapartcompiti.value!!.isNotEmpty()) {
                    if (binding.CompNameText.text.toString() != "") {
                        compViewModel.setnome(binding.CompNameText.text.toString())
                        singleLegaVM.creacompetizione(compViewModel.competizione.value!!)
                    } else {
                        val alertDialog = AlertDialog.Builder(
                            requireContext(),
                            androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                        ).create()
                        alertDialog.setTitle("ATTENZIONE")
                        alertDialog.setMessage("Non hai inserito un nome per la competizione")
                        alertDialog.setButton(
                            AlertDialog.BUTTON_NEGATIVE, "OK"
                        ) { dialog, which -> dialog.dismiss() }
                        alertDialog.show()
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                            .setTextColor(Color.parseColor("#ff5722"))
                    }
                } else {
                    val alertDialog = AlertDialog.Builder(
                        requireContext(),
                        androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                    ).create()
                    alertDialog.setTitle("ATTENZIONE")
                    alertDialog.setMessage("Non hai selezionato nessun partecipante")
                    alertDialog.setButton(
                        AlertDialog.BUTTON_NEGATIVE, "OK"
                    ) { dialog, which -> dialog.dismiss() }
                    alertDialog.show()
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(Color.parseColor("#ff5722"))
                }
            } else {
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("ATTENZIONE")
                alertDialog.setMessage("Non hai selezionato il tipo di competizione")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.parseColor("#ff5722"))
            }
        }
        singleLegaVM.creando.observe(requireActivity()){
            if(it==true){
                binding.progressBar.visibility=View.VISIBLE
            }
            else if(it==false){
                binding.progressBar.visibility=View.GONE
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("SUCCESSO")
                alertDialog.setMessage("Hai creato una nuova competizione")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.parseColor("#ff5722"))
                parentFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<LegaView>(R.id.legafragmentcontainer)
                    parentFragmentManager.popBackStackImmediate("CreaComp", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
                singleLegaVM.resetcreando()
            }
        }

    }


}