package com.example.dreamteammanager.competizione

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.R
import com.example.dreamteammanager.databinding.FragmentCompetizioneViewBinding
import com.example.dreamteammanager.lega.PartecipantiAdapter
import com.example.dreamteammanager.viewmodel.CompetizioniVM
import com.example.dreamteammanager.viewmodel.ImagesVM
import com.example.dreamteammanager.viewmodel.SharedPreferencesManager
import com.example.dreamteammanager.viewmodel.parseJsonToModel


class CompetizioneViewFragment : Fragment() {
    lateinit var binding: FragmentCompetizioneViewBinding
    private val compVM: CompetizioniVM by activityViewModels()
    private val imagesVM: ImagesVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val utente = parseJsonToModel(SharedPreferencesManager.getString("utente", ""))
        if (utente.id != compVM.idamm.value) {
            binding.CalcolaGiornataButton.visibility = View.GONE
            binding.CaricaGiocatoriButton.visibility = View.GONE
        }
        compVM.getpartecipanti()
        binding.NomeComp.text = compVM.competizione.value?.nome
        when (compVM.competizione.value?.sport) {
            "Serie A" -> binding.immagine.setImageResource(R.drawable.seriealogo)
            "MotoGP" -> binding.immagine.setImageResource(R.drawable.motogplogo)
            "Formula Uno" -> binding.immagine.setImageResource(R.drawable.formula_1)
        }
        compVM.scaricando.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else if (it == false) {
                val rv = binding.recViewComp
                val adapter =
                    PartecipantiAdapter(
                        compVM.partecipanti.value!!,
                        false,
                        imagesVM,
                        compVM.idamm.value
                    )
                rv.layoutManager = LinearLayoutManager(context)
                rv.adapter = adapter
                binding.progressBar.visibility = View.GONE
                compVM.resetscaricando()
            }
        }
        val calcoladialog = Dialog(requireContext())
        binding.CalcolaGiornataButton.setOnClickListener {
            compVM.getgiornatedacalcolare()
        }
        compVM.prendendo.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else if (it == false) {
                binding.progressBar.visibility = View.GONE
                val array = mutableListOf<String>()
                compVM.giornatedacalcolare.value?.forEach {
                    array.add(it.giornata.toString())
                }
                calcoladialog.setContentView(R.layout.dialog_calcola_giornata)
                calcoladialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    requireContext(), android.R.layout.simple_spinner_dropdown_item, array
                )
                calcoladialog.findViewById<Spinner>(R.id.spinner)
                    .adapter = spinnerArrayAdapter
                calcoladialog.findViewById<Button>(R.id.CalcolaGiornataButton).setOnClickListener {
                    compVM.calcolagiornata(
                        calcoladialog.findViewById<Spinner>(R.id.spinner).selectedItem.toString()
                            .toInt()
                    )
                    calcoladialog.dismiss()
                }
                calcoladialog.show()
                compVM.resetprendendo()

            }
        }
        compVM.calcolando.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else if (it == false) {
                binding.progressBar.visibility = View.GONE
                calcoladialog.dismiss()
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("SUCCESSO")
                alertDialog.setMessage("Hai correttamento calcolato la giornata")
                alertDialog.setButton(
                    AlertDialog.BUTTON_POSITIVE, "OK",
                ) { dialog, which ->
                    dialog.dismiss()
                }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(Color.parseColor("#ff5722"))
                compVM.resetcalcolando()
            }
        }
        val classificaDialog = Dialog(requireContext())
        binding.ClassificaButton.setOnClickListener {
            compVM.getClassifica()

        }
        compVM.scaricandoclassifica.observe(requireActivity()){
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            }else if (it == false){
                binding.progressBar.visibility = View.GONE
                classificaDialog.setContentView(R.layout.dialog_classifica)
                classificaDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val recyclerView = classificaDialog.findViewById<RecyclerView>(R.id.recviewstatistiche)

                recyclerView.layoutManager = LinearLayoutManager(context)
                val  adapter = ClassificaAdapter(compVM.classifica.value!!, imagesVM)
                recyclerView.adapter = adapter
                classificaDialog.show()
                compVM.resetscaricandoclassifica()

            }


        }

        val statisticheDialog = Dialog(requireContext())
        binding.StatisticheButton.setOnClickListener {
           compVM.getStatistica()
        }

        compVM.scaricandostatistiche.observe(requireActivity()){
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            }else if (it == false){
                binding.progressBar.visibility = View.GONE
                statisticheDialog.setContentView(R.layout.dialog_statistiche)
                statisticheDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val recyclerView = statisticheDialog.findViewById<RecyclerView>(R.id.recviewstatistiche)
                recyclerView.layoutManager = LinearLayoutManager(context)
                val  adapter = StatisticheAdapter(compVM.statistiche.value!!,compVM.competizione.value!!.sport)
                recyclerView.adapter = adapter
                statisticheDialog.show()
                compVM.resetscaricandostatistiche()

            }


        }

        binding.InsertFormazioneButton.setOnClickListener {
            if(compVM.competizione.value?.sport == "Serie A"){
                    parentFragmentManager.commit {
                        setReorderingAllowed(true)
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        replace<InserisciFormazioneFragment>(R.id.fragmentContainerView, "FORMAZIONE")
                        addToBackStack("Formazione Fragment")
                    }


            }else if(compVM.competizione.value?.sport == "MotoGP" || compVM.competizione.value?.sport == "Formula Uno"){
                    parentFragmentManager.commit {
                        setReorderingAllowed(true)
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        replace<InserisciGrigliaFragment>(R.id.fragmentContainerView, "GRIGLIA")
                        addToBackStack("Griglia Fragment")
                    }
            }else{
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("ERRORE")
                alertDialog.setMessage("Qualcosa è andata storto")
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

        binding.CaricaGiocatoriButton.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                replace<CaricaGiocatoriFragment>(R.id.competizioni_cont_view, "CARICAGIOCATORI")
                addToBackStack("LoadGiocatori")
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompetizioneViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}