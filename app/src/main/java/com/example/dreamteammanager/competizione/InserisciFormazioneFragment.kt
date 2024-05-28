package com.example.dreamteammanager.competizione

import android.app.Dialog
import android.content.res.Resources.Theme
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.GiocatoreFormazione
import com.example.dreamteammanager.databinding.FragmentInserisciFormazioneBinding
import com.example.dreamteammanager.viewmodel.CompetizioniVM
import com.example.dreamteammanager.viewmodel.ImagesVM
import com.example.dreamteammanager.viewmodel.SharedPreferencesManager
import com.example.dreamteammanager.viewmodel.SingleLegaVM
import com.example.dreamteammanager.viewmodel.parseJsonToModel

class InserisciFormazioneFragment : Fragment() {
    lateinit var binding: FragmentInserisciFormazioneBinding
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
        binding = FragmentInserisciFormazioneBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        compViewModel.clear()
        val array = mutableListOf<String>()
        compViewModel.giornate.value?.forEach {
            array.add(it.giornata.toString())
        }
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, array
        )
        binding.giornataSpinner.adapter = spinnerArrayAdapter

        val formazioneDialog = Dialog(requireContext())
        binding.cardView1.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView =
                formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)
            val portieri = compViewModel.filtro("P")
            val adapter = InserisciFormazioneAdapter(portieri)
            adapter.setonclick(
                object : InserisciFormazioneAdapter.SetOnClickListener {
                    override fun onClick(position: Int, giocatore: GiocatoreFormazione) {
                        compViewModel.formazione.value!![0] = giocatore.id
                        binding.portiere.text = giocatore.nome
                        binding.portiere.setTextColor(requireContext().getColor(R.color.arancione))
                        formazioneDialog.dismiss()
                    }
                }
            )

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            formazioneDialog.show()
        }

        binding.cardView2.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView =
                formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)
            val difensori = compViewModel.filtro("D")
            val adapter = InserisciFormazioneAdapter(difensori)
            adapter.setonclick(
                object : InserisciFormazioneAdapter.SetOnClickListener {
                    override fun onClick(position: Int, giocatore: GiocatoreFormazione) {
                        compViewModel.formazione.value!![1] = giocatore.id
                        binding.difensore.text = giocatore.nome
                        binding.difensore.setTextColor(requireContext().getColor(R.color.arancione))
                        formazioneDialog.dismiss()
                    }
                }
            )

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter

            formazioneDialog.show()
        }
        binding.cardView3.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView =
                formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)
            val difensori = compViewModel.filtro("D")
            val adapter = InserisciFormazioneAdapter(difensori)
            adapter.setonclick(
                object : InserisciFormazioneAdapter.SetOnClickListener {
                    override fun onClick(position: Int, giocatore: GiocatoreFormazione) {
                        compViewModel.formazione.value!![2] = giocatore.id
                        binding.difensore2.text = giocatore.nome
                        binding.difensore2.setTextColor(requireContext().getColor(R.color.arancione))
                        formazioneDialog.dismiss()
                    }
                }
            )

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter

            formazioneDialog.show()
        }
        binding.cardView4.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView =
                formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)
            val difensori = compViewModel.filtro("D")
            val adapter = InserisciFormazioneAdapter(difensori)
            adapter.setonclick(
                object : InserisciFormazioneAdapter.SetOnClickListener {
                    override fun onClick(position: Int, giocatore: GiocatoreFormazione) {
                        compViewModel.formazione.value!![3] = giocatore.id
                        binding.difensore3.text = giocatore.nome
                        binding.difensore3.setTextColor(requireContext().getColor(R.color.arancione))
                        formazioneDialog.dismiss()
                    }
                }
            )

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter

            formazioneDialog.show()
        }
        binding.cardView5.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView =
                formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)
            val difensori = compViewModel.filtro("D")
            val adapter = InserisciFormazioneAdapter(difensori)
            adapter.setonclick(
                object : InserisciFormazioneAdapter.SetOnClickListener {
                    override fun onClick(position: Int, giocatore: GiocatoreFormazione) {
                        compViewModel.formazione.value!![4] = giocatore.id
                        binding.difensore4.text = giocatore.nome
                        binding.difensore4.setTextColor(requireContext().getColor(R.color.arancione))
                        formazioneDialog.dismiss()
                    }
                }
            )

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter

            formazioneDialog.show()
        }

        binding.cardView6.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView =
                formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)
            val centrocampisti = compViewModel.filtro("C")
            val adapter = InserisciFormazioneAdapter(centrocampisti)
            adapter.setonclick(
                object : InserisciFormazioneAdapter.SetOnClickListener {
                    override fun onClick(position: Int, giocatore: GiocatoreFormazione) {
                        compViewModel.formazione.value!![5] = giocatore.id
                        binding.centro.text = giocatore.nome
                        binding.centro.setTextColor(requireContext().getColor(R.color.arancione))
                        formazioneDialog.dismiss()
                    }
                }
            )

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter

            formazioneDialog.show()
        }

        binding.cardView7.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView =
                formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)
            val centrocampisti = compViewModel.filtro("C")
            val adapter = InserisciFormazioneAdapter(centrocampisti)
            adapter.setonclick(
                object : InserisciFormazioneAdapter.SetOnClickListener {
                    override fun onClick(position: Int, giocatore: GiocatoreFormazione) {
                        compViewModel.formazione.value!![6] = giocatore.id
                        binding.centro2.text = giocatore.nome
                        binding.centro2.setTextColor(requireContext().getColor(R.color.arancione))
                        formazioneDialog.dismiss()
                    }
                }
            )

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter

            formazioneDialog.show()
        }

        binding.cardView8.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView =
                formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)
            val centrocampisti = compViewModel.filtro("C")
            val adapter = InserisciFormazioneAdapter(centrocampisti)
            adapter.setonclick(
                object : InserisciFormazioneAdapter.SetOnClickListener {
                    override fun onClick(position: Int, giocatore: GiocatoreFormazione) {
                        compViewModel.formazione.value!![7] = giocatore.id
                        binding.centro3.text = giocatore.nome
                        binding.centro3.setTextColor(requireContext().getColor(R.color.arancione))
                        formazioneDialog.dismiss()
                    }
                }
            )
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            formazioneDialog.show()
        }
        binding.cardView9.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView =
                formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)
            val attaccanti = compViewModel.filtro("A")
            val adapter = InserisciFormazioneAdapter(attaccanti)
            adapter.setonclick(
                object : InserisciFormazioneAdapter.SetOnClickListener {
                    override fun onClick(position: Int, giocatore: GiocatoreFormazione) {
                        compViewModel.formazione.value!![8] = giocatore.id
                        binding.att.text = giocatore.nome
                        binding.att.setTextColor(requireContext().getColor(R.color.arancione))
                        formazioneDialog.dismiss()
                    }
                }
            )

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter

            formazioneDialog.show()
        }

        binding.cardView10.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView =
                formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)
            val attaccanti = compViewModel.filtro("A")
            val adapter = InserisciFormazioneAdapter(attaccanti)
            adapter.setonclick(
                object : InserisciFormazioneAdapter.SetOnClickListener {
                    override fun onClick(position: Int, giocatore: GiocatoreFormazione) {
                        compViewModel.formazione.value!![9] = giocatore.id
                        binding.att2.text = giocatore.nome
                        binding.att2.setTextColor(requireContext().getColor(R.color.arancione))
                        formazioneDialog.dismiss()
                    }
                }
            )

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            formazioneDialog.show()
        }
        binding.cardView11.setOnClickListener {
            formazioneDialog.setContentView(R.layout.dialog_inserisci_formazione)
            formazioneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val recyclerView =
                formazioneDialog.findViewById<RecyclerView>(R.id.recviewscegligiocatori)
            val attaccanti = compViewModel.filtro("A")
            val adapter = InserisciFormazioneAdapter(attaccanti)
            adapter.setonclick(
                object : InserisciFormazioneAdapter.SetOnClickListener {
                    override fun onClick(position: Int, giocatore: GiocatoreFormazione) {
                        compViewModel.formazione.value!![10] = giocatore.id
                        binding.att3.text = giocatore.nome
                        binding.att3.setTextColor(requireContext().getColor(R.color.arancione))
                        formazioneDialog.dismiss()
                    }
                }
            )
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            formazioneDialog.show()
        }
        binding.CreaButton.setOnClickListener {
            if (compViewModel.checkForm()) {
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
                alertDialog.setMessage("Mancano giocatori alla formazione")
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
                alertDialog.setMessage("Hai inserito la formazione")
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