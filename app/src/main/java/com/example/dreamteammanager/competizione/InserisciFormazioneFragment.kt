package com.example.dreamteammanager.competizione

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.R
import com.example.dreamteammanager.databinding.FragmentInserisciFormazioneBinding
import com.example.dreamteammanager.viewmodel.CompetizioniVM
import com.example.dreamteammanager.viewmodel.ImagesVM
import com.example.dreamteammanager.viewmodel.SingleLegaVM

class InserisciFormazioneFragment : Fragment() {
    lateinit var binding: FragmentInserisciFormazioneBinding
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
        binding = FragmentInserisciFormazioneBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val formazioneDialog = Dialog(requireContext())
        binding.cardView1.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView = formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)

            // inizializzare l'adapter
            //val adapter = InserisciFormazioneAdapter()


            //recyclerView.adapter = adapter

            formazioneDialog.show()
        }

        binding.cardView2.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView = formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)

            // inizializzare l'adapter
            //val adapter = InserisciFormazioneAdapter()


            //recyclerView.adapter = adapter

            formazioneDialog.show()
        }
        binding.cardView3.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView = formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)

            // inizializzare l'adapter
            //val adapter = InserisciFormazioneAdapter()


            //recyclerView.adapter = adapter

            formazioneDialog.show()
        }
        binding.cardView4.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView = formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)

            // inizializzare l'adapter
            //val adapter = InserisciFormazioneAdapter()


            //recyclerView.adapter = adapter

            formazioneDialog.show()
        }

        binding.cardView5.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView = formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)

            // inizializzare l'adapter
            //val adapter = InserisciFormazioneAdapter()


            //recyclerView.adapter = adapter

            formazioneDialog.show()
        }

        binding.cardView6.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView = formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)

            // inizializzare l'adapter
            //val adapter = InserisciFormazioneAdapter()


            //recyclerView.adapter = adapter

            formazioneDialog.show()
        }

        binding.cardView7.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView = formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)

            // inizializzare l'adapter
            //val adapter = InserisciFormazioneAdapter()


            //recyclerView.adapter = adapter

            formazioneDialog.show()
        }

        binding.cardView8.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView = formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)

            // inizializzare l'adapter
           //val adapter = InserisciFormazioneAdapter()


            //recyclerView.adapter = adapter

            formazioneDialog.show()
        }

        binding.cardView9.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView = formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)

            // inizializzare l'adapter
            //val adapter = InserisciFormazioneAdapter()


            //recyclerView.adapter = adapter

            formazioneDialog.show()
        }

        binding.cardView10.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView = formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)

            // inizializzare l'adapter
            //val adapter = InserisciFormazioneAdapter()


            //recyclerView.adapter = adapter

            formazioneDialog.show()
        }
        binding.cardView11.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView = formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)

            // inizializzare l'adapter
            //val adapter = InserisciFormazioneAdapter()
            //recyclerView.adapter = adapter

            formazioneDialog.show()
        }


    }

}