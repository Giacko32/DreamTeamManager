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
import android.widget.ArrayAdapter
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
import com.example.dreamteammanager.classi.GiocatoreFormazione
import com.example.dreamteammanager.classi.Pilota
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.classi.Utils.Companion.parseJsonToModel
import com.example.dreamteammanager.databinding.FragmentCreaCompetizioneBinding
import com.example.dreamteammanager.databinding.FragmentInserisciFormazioneBinding
import com.example.dreamteammanager.databinding.FragmentInserisciGrigliaBinding
import com.example.dreamteammanager.lega.InvitaFragment
import com.example.dreamteammanager.lega.LegaView
import com.example.dreamteammanager.lega.PartecipantiAdapter
import com.example.dreamteammanager.viewmodel.CompetizioniVM
import com.example.dreamteammanager.viewmodel.ImagesVM
import com.example.dreamteammanager.viewmodel.SharedPreferencesManager
import com.example.dreamteammanager.viewmodel.SingleLegaVM

class InserisciGrigliaFragment : Fragment() {
    lateinit var binding: FragmentInserisciGrigliaBinding
    private val compViewModel: CompetizioniVM by activityViewModels()
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
        compViewModel.clear()
        val array = mutableListOf<String>()
        compViewModel.giornate.value?.forEach {
            Log.d("giornata", it.giornata.toString())
            array.add(it.giornata.toString())
        }
        Log.d("array", array.toString())
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, array
        )
        binding.giornataSpinner.adapter = spinnerArrayAdapter
        val grigliaDialog = Dialog(requireContext())
        binding.cardView1.setOnClickListener {
            grigliaDialog.setContentView(R.layout.dialog_inserisci_griglia)
            grigliaDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView = grigliaDialog.findViewById<RecyclerView>(R.id.recviewsceglipiloti)
            val piloti=compViewModel.filtrapiloti()
            val adapter = InserisciGrigliaAdapter(piloti)
            adapter.setonclick(
                object : InserisciGrigliaAdapter.SetOnClickListener {
                    override fun onClick(position: Int, pilota: Pilota) {
                        compViewModel.griglia.value!![0] = pilota.idpilota
                        binding.pilota1.text = pilota.nome
                        binding.pilota1.setTextColor(requireContext().getColor(R.color.arancione))
                        grigliaDialog.dismiss()
                    }
                }
            )

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            grigliaDialog.show()
        }

        binding.cardView2.setOnClickListener {
            grigliaDialog.setContentView(R.layout.dialog_inserisci_griglia)
            grigliaDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView = grigliaDialog.findViewById<RecyclerView>(R.id.recviewsceglipiloti)
            val piloti=compViewModel.filtrapiloti()
            val adapter = InserisciGrigliaAdapter(piloti)
            adapter.setonclick(
                object : InserisciGrigliaAdapter.SetOnClickListener {
                    override fun onClick(position: Int, pilota: Pilota) {
                        compViewModel.griglia.value!![1] = pilota.idpilota
                        binding.pilota2.text = pilota.nome
                        binding.pilota2.setTextColor(requireContext().getColor(R.color.arancione))
                        grigliaDialog.dismiss()
                    }
                }
            )
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            grigliaDialog.show()
        }
        binding.CreaButton.setOnClickListener {
            if (compViewModel.checkGriglia()) {
                compViewModel.inseresciFormazione(
                    parseJsonToModel(
                        SharedPreferencesManager.getString(
                            "utente",
                            ""
                        )
                    ), binding.giornataSpinner.selectedItem.toString().toInt()
                )
            } else {
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("ATTENZIONE")
                alertDialog.setMessage("Mancano piloti alla griglia")
                alertDialog.setButton(
                    AlertDialog.BUTTON_POSITIVE, "OK",
                ) { dialog, which ->
                    dialog.dismiss()
                }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(Color.parseColor("#ff5722"))
            }
        }
        compViewModel.aggiungendoformazione.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else if (it == false) {
                binding.progressBar.visibility = View.GONE
                compViewModel.resetaggiungendoformazione()
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("SUCCESSO")
                alertDialog.setMessage("Hai inserito la griglia")
                alertDialog.setButton(
                    AlertDialog.BUTTON_POSITIVE, "OK",
                ) { dialog, which ->
                    dialog.dismiss()
                }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(Color.parseColor("#ff5722"))
                parentFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<CompetizioneViewFragment>(R.id.competizioni_cont_view)
                    addToBackStack(null)
                }
            }
        }



    }

}