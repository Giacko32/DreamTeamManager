package com.example.dreamteammanager.lega

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Lega
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.databinding.FragmentRichiesteBinding
import com.example.dreamteammanager.main.LegheAdapter
import com.example.dreamteammanager.viewmodel.ImagesVM
import com.example.dreamteammanager.viewmodel.SharedPreferencesManager
import com.example.dreamteammanager.viewmodel.SingleLegaVM
import com.example.dreamteammanager.viewmodel.parseJsonToLega
import kotlin.math.sin


class RichiesteFragment : Fragment() {
    lateinit var binding: FragmentRichiesteBinding
    private val singleLegaVM: SingleLegaVM by activityViewModels()
    private val imagesVM: ImagesVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val AccettazioneDialog = Dialog(requireActivity())
        singleLegaVM.getrichiedenti()
        singleLegaVM.scaricando.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else if (it == false) {
                singleLegaVM.updatecaricadati()
                singleLegaVM.resetscaricando()
            }

        }
        singleLegaVM.accettando.observe(viewLifecycleOwner){
            if(it==true){
                binding.progressBar.visibility = View.VISIBLE
            }else if(it==false){
                singleLegaVM.updatecaricadati()
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("SUCCESSO")
                alertDialog.setMessage("Utente accettato nella lega")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.parseColor("#ff5722"))
                binding.progressBar.visibility = View.GONE
                singleLegaVM.resetaccettando()
            }
        }
        singleLegaVM.caricadati.observe(viewLifecycleOwner){
            if(it==true){
                val adapter = PartecipantiAdapter(singleLegaVM.richiedenti.value!!, false, imagesVM)
                adapter.setonclick(object : PartecipantiAdapter.SetOnClickListener {
                    override fun onClick(position: Int, utente: Utente) {
                        binding.progressBar.visibility = View.GONE
                        AccettazioneDialog.setContentView(R.layout.fragment_custom_dialog)
                        AccettazioneDialog.findViewById<TextView>(R.id.dialogTitle).setText(
                            "Vuoi accettare nella lega ${utente.username}?"
                        )
                        AccettazioneDialog.findViewById<Button>(R.id.yesButton).setOnClickListener {
                            singleLegaVM.eliminarichiedente(utente.id)
                            singleLegaVM.accettautente(utente.id)
                            AccettazioneDialog.dismiss()
                        }
                        AccettazioneDialog.findViewById<Button>(R.id.noButton).setOnClickListener {
                            singleLegaVM.eliminarichiedente(utente.id)
                            singleLegaVM.rifiutautente(utente.id)
                            val alertDialog = AlertDialog.Builder(
                                requireContext(),
                                androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                            ).create()
                            alertDialog.setTitle("SUCCESSO")
                            alertDialog.setMessage("Utente rifiutato nella lega")
                            alertDialog.setButton(
                                AlertDialog.BUTTON_NEGATIVE, "OK"
                            ) { dialog, which -> dialog.dismiss() }
                            alertDialog.show()
                            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                                .setTextColor(Color.parseColor("#ff5722"))
                            singleLegaVM.updatecaricadati()
                            AccettazioneDialog.dismiss()
                        }
                        AccettazioneDialog.show()

                    }
                })
                binding.rv.layoutManager = LinearLayoutManager(context)
                binding.rv.adapter = adapter
                binding.progressBar.visibility = View.GONE
                singleLegaVM.resetcaricadati()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRichiesteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}