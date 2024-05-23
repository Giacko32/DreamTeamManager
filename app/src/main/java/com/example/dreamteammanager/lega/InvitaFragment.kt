package com.example.dreamteammanager.lega

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.databinding.FragmentInvitaBinding
import com.example.dreamteammanager.databinding.FragmentMainBinding
import com.example.dreamteammanager.viewmodel.ImagesVM
import com.example.dreamteammanager.viewmodel.SingleLegaVM


class InvitaFragment : Fragment() {
    lateinit var binding: FragmentInvitaBinding
    private val singleLegaVM: SingleLegaVM by activityViewModels()
    private val imagesVM: ImagesVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInvitaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val InvitaDialog = Dialog(requireActivity())
        var adapter: PartecipantiAdapter
        val rvInvita = binding.recyclerViewInvita
        binding.searchIcon.setOnClickListener {
            val testo = binding.searched.text.toString()
            singleLegaVM.setinvitatiFiltrato(testo)
            singleLegaVM.updatefiltrati()
            singleLegaVM.updatecaricadati()
        }
        singleLegaVM.getutentiinivito()
        singleLegaVM.utentiinvito.observe(viewLifecycleOwner) {
            if (it == true) {
                singleLegaVM.resetutentiinvito()
                singleLegaVM.updatecaricadati()
                binding.progressBar.visibility = View.GONE
            } else if (it == false) {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
        singleLegaVM.invitando.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else if (it == false) {
                singleLegaVM.updatecaricadati()
                binding.progressBar.visibility = View.GONE
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("SUCCESSO")
                alertDialog.setMessage("Utente invitato nella lega")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.parseColor("#ff5722"))
                singleLegaVM.resetinvitando()

            }
        }
        singleLegaVM.caricadati.observe(viewLifecycleOwner) {
            if (it == true) {
                if (singleLegaVM.filtrati.value == true) {
                    adapter = PartecipantiAdapter(
                        singleLegaVM.invitatifiltrato.value!!,
                        false,
                        imagesVM,
                        null
                    )
                    singleLegaVM.resetfiltrati()
                } else{
                    adapter = PartecipantiAdapter(
                        singleLegaVM.invitati.value!!,
                        false,
                        imagesVM,
                        null
                    )
                }
                adapter.setonclick(object : PartecipantiAdapter.SetOnClickListener {
                    override fun onClick(position: Int, utente: Utente) {
                        InvitaDialog.setContentView(R.layout.fragment_custom_dialog)
                        InvitaDialog.findViewById<TextView>(R.id.dialogTitle).setText(
                            "Vuoi invitare nella lega ${utente.username}?"
                        )
                        InvitaDialog.findViewById<Button>(R.id.yesButton).setOnClickListener {
                            singleLegaVM.eliminainvitato(utente.id)
                            singleLegaVM.updatecaricadati()
                            singleLegaVM.invitautente(utente.id)
                            InvitaDialog.dismiss()
                        }
                        InvitaDialog.findViewById<Button>(R.id.noButton).setOnClickListener {
                            singleLegaVM.eliminainvitato(utente.id)
                            singleLegaVM.updatecaricadati()
                            InvitaDialog.dismiss()
                        }
                        InvitaDialog.show()

                    }
                }
                )
                rvInvita.adapter = adapter
                rvInvita.layoutManager = LinearLayoutManager(requireActivity())
                singleLegaVM.resetcaricadati()
            }
        }


    }


}