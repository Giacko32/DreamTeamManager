package com.example.dreamteammanager.competizione

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Competizione
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.databinding.FragmentCreaCompetizioneBinding
import com.example.dreamteammanager.databinding.FragmentInserisciFormazioneBinding
import com.example.dreamteammanager.databinding.FragmentInserisciGrigliaBinding
import com.example.dreamteammanager.lega.InvitaFragment
import com.example.dreamteammanager.lega.LegaView
import com.example.dreamteammanager.lega.PartecipantiAdapter
import com.example.dreamteammanager.viewmodel.CompetizioniVM
import com.example.dreamteammanager.viewmodel.ImagesVM
import com.example.dreamteammanager.viewmodel.SingleLegaVM
import kotlin.math.sin

class InserisciGrigliaFragment : Fragment() {
    lateinit var binding: FragmentInserisciGrigliaBinding
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
        binding = FragmentInserisciGrigliaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val grigliaDialog = Dialog(requireContext())
        binding.cardView1.setOnClickListener {
            grigliaDialog.setContentView(R.layout.dialog_inserisci_griglia)
            grigliaDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView = grigliaDialog.findViewById<RecyclerView>(R.id.recviewsceglipiloti)

            // inizializzare l'adapter
            val adapter = InserisciGrigliaAdapter()


            recyclerView.adapter = adapter

            grigliaDialog.show()
        }

        binding.cardView2.setOnClickListener {
            grigliaDialog.setContentView(R.layout.dialog_inserisci_griglia)
            grigliaDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView = grigliaDialog.findViewById<RecyclerView>(R.id.recviewsceglipiloti)

            // inizializzare l'adapter
            val adapter = InserisciGrigliaAdapter()


            recyclerView.adapter = adapter

            grigliaDialog.show()
        }



    }

}