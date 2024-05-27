package com.example.dreamteammanager.competizione

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Competizione
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

        competizioniVM.getpartecipanti()
        competizioniVM.scaricando.observe(viewLifecycleOwner) {
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
                adapter.setonclick(object: PartecipantiAdapter.SetOnClickListener {
                    override fun onClick(position: Int, utente: Utente) {
                        val selectDialog = Dialog(requireActivity())
                        selectDialog.setContentView(R.layout.dialog_inserisci_formazione)
                        selectDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        selectDialog.show()
                    }
                })
                rv.layoutManager = LinearLayoutManager(context)
                rv.adapter = adapter
                binding.progressBar.visibility = View.GONE
                competizioniVM.resetscaricando()
            }
        }

    }



}