package com.example.dreamteammanager.competizione

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.GiocatoreFormazione
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.databinding.FragmentCaricaGiocatoriBinding
import com.example.dreamteammanager.lega.PartecipantiAdapter
import com.example.dreamteammanager.viewmodel.CompetizioniVM
import com.example.dreamteammanager.viewmodel.ImagesVM


class CaricaGiocatoriFragment : Fragment() {

    lateinit var binding: FragmentCaricaGiocatoriBinding
    val competizioniVM: CompetizioniVM by activityViewModels()
    val imagesVM: ImagesVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCaricaGiocatoriBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(competizioniVM.competizione.value!!.sport != "Serie A"){
            binding.textCarica.text = "Carica piloti"
        }
        competizioniVM.getgiocatoridisp()
        competizioniVM.scaricandogioc.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else if (it == false) {
                val rv = binding.rcvPartecipanti
                val adapter =
                    PartecipantiAdapter(
                        competizioniVM.partecipanti.value!!,
                        false,
                        imagesVM,
                        null
                    )
                adapter.setonclick(object : PartecipantiAdapter.SetOnClickListener {
                    override fun onClick(position: Int, utente: Utente) {
                        val selectDialog = Dialog(requireActivity())
                        selectDialog.setContentView(R.layout.dialog_inserisci_giocorpiloticomp)
                        selectDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        if(competizioniVM.competizione.value!!.sport != "Serie A"){
                            selectDialog.findViewById<TextView>(R.id.scegligiocatoreTitle).text = "Scegli pilota"
                        }
                        selectDialog.findViewById<TextView>(R.id.searched).addTextChangedListener(textWatcher)
                        val recv =
                            selectDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)
                        val adapter2 =
                            InserisciFormazioneAdapter(competizioniVM.giocatoridisp.value!!)
                        adapter2.setonclick(object : InserisciFormazioneAdapter.SetOnClickListener {
                            override fun onClick(position: Int, gioc: GiocatoreFormazione) {
                                val choiceDialog = Dialog(requireActivity())
                                choiceDialog.setContentView(R.layout.fragment_custom_dialog)
                                choiceDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                if (competizioniVM.competizione.value!!.sport == "Serie A") {
                                    choiceDialog.findViewById<TextView>(R.id.dialogTitle).text =
                                        "Sicuro di voler aggiungere questo giocatore?"
                                } else {
                                    choiceDialog.findViewById<TextView>(R.id.dialogTitle).text =
                                        "Sicuro di voler aggiungere questo pilota?"
                                }
                                choiceDialog.findViewById<Button>(R.id.yesButton)
                                    .setOnClickListener {
                                        competizioniVM.inseriscigiocatore(
                                            gioc,
                                            utente
                                        )

                                        choiceDialog.dismiss()
                                        selectDialog.dismiss()
                                        competizioniVM.resetfiltro()
                                    }
                                choiceDialog.findViewById<Button>(R.id.noButton)
                                    .setOnClickListener {
                                        choiceDialog.dismiss()
                                    }
                                choiceDialog.show()
                            }

                        })
                        recv.layoutManager = LinearLayoutManager(context)
                        recv.adapter = adapter2
                        selectDialog.show()
                        competizioniVM.filtrate.observe(viewLifecycleOwner) {
                            if (it != null) {
                                if (it == true) {
                                    val adapter3 =
                                        InserisciFormazioneAdapter(competizioniVM.giocatoridispfil.value!!)
                                    adapter3.setonclick(object : InserisciFormazioneAdapter.SetOnClickListener {
                                        override fun onClick(position: Int, gioc: GiocatoreFormazione) {
                                            val choiceDialog = Dialog(requireActivity())
                                            choiceDialog.setContentView(R.layout.fragment_custom_dialog)
                                            choiceDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                            if (competizioniVM.competizione.value!!.sport == "Serie A") {
                                                choiceDialog.findViewById<TextView>(R.id.dialogTitle).text =
                                                    "Sicuro di voler aggiungere questo giocatore?"
                                            } else {
                                                choiceDialog.findViewById<TextView>(R.id.dialogTitle).text =
                                                    "Sicuro di voler aggiungere questo pilota?"
                                            }
                                            choiceDialog.findViewById<Button>(R.id.yesButton)
                                                .setOnClickListener {
                                                    competizioniVM.inseriscigiocatore(
                                                        gioc,
                                                        utente
                                                    )
                                                    choiceDialog.dismiss()
                                                    selectDialog.dismiss()
                                                    competizioniVM.resetfiltro()
                                                }
                                            choiceDialog.findViewById<Button>(R.id.noButton)
                                                .setOnClickListener {
                                                    choiceDialog.dismiss()
                                                }
                                            choiceDialog.show()
                                        }

                                    })
                                    recv.layoutManager = LinearLayoutManager(context)
                                    recv.adapter = adapter3

                                }
                            }
                        }
                    }
                })
                rv.layoutManager = LinearLayoutManager(context)
                rv.adapter = adapter
                binding.progressBar.visibility = View.GONE
                competizioniVM.resetscaricandogioc()
            }
        }

        competizioniVM.aggiungendogioc.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else if (it == false) {
                var mex = ""
                if (competizioniVM.competizione.value!!.sport == "Serie A") {
                    mex = "Hai correttamento caricato il giocatore nella rosa"
                } else {
                    mex = "Hai correttamento caricato il pilota nella rosa"
                }
                binding.progressBar.visibility = View.GONE
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("SUCCESSO")
                alertDialog.setMessage(mex)
                alertDialog.setButton(
                    AlertDialog.BUTTON_POSITIVE, "OK",
                ) { dialog, which ->
                    dialog.dismiss()
                }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(Color.parseColor("#ff5722"))
                competizioniVM.resetaggiungendogioc()
            }
        }


    }

    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Code to execute before the text is changed
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            competizioniVM.filtrogioc(s.toString())
        }
    }
}





