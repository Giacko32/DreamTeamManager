package com.example.dreamteammanager.main

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Lega
import com.example.dreamteammanager.databinding.FragmentMainBinding
import com.example.dreamteammanager.lega.LegaActivity
import com.example.dreamteammanager.viewmodel.ImagesVM
import com.example.dreamteammanager.viewmodel.SharedPreferencesManager
import com.example.dreamteammanager.viewmodel.legheVM
import com.example.dreamteammanager.viewmodel.parseJsonToModel
import com.example.dreamteammanager.viewmodel.parseModelToJson


class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    private val legheVM: legheVM by viewModels()
    private val imagesVM: ImagesVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val utente = parseJsonToModel(
            SharedPreferencesManager.getString(
                "utente",
                ""
            )
        )
        Log.d("UTENTEBESTIA", utente.id.toString())
        legheVM.resetcheckrichiesta()
        legheVM.scaricaleghe(utente.id)
        val creaLegaDialog = Dialog(requireActivity())
        val IscrizioneDialog = Dialog(requireActivity())

        binding.creaNuovaLegaButton.setOnClickListener {
            val newLega = Lega(0, "", 1, 1, utente.id)
            creaLegaDialog.setContentView(R.layout.dialog_crea_lega)
            creaLegaDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val legaimg = creaLegaDialog.findViewById<ImageView>(R.id.image)
            imagesVM.getLegaImage(requireContext(), newLega.image, legaimg)
            creaLegaDialog.findViewById<TextView>(R.id.changeImage).setOnClickListener {
                newLega.image = (newLega.image%4) + 1
                imagesVM.getLegaImage(requireContext(), newLega.image, legaimg)
            }
            creaLegaDialog.findViewById<Button>(R.id.creaLegaButton).setOnClickListener {
                newLega.name = creaLegaDialog.findViewById<TextView>(R.id.NomeLega).text.toString()
                legheVM.creanuovalega(newLega)
                creaLegaDialog.dismiss()
            }
            creaLegaDialog.show()


        }

        legheVM.scaricando.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else if (it == false) {
                binding.progressBar.visibility = View.GONE
            }
        }

        legheVM.listaLeghe.observe(viewLifecycleOwner) {
            val adapter = LegheAdapter(it, imagesVM, false)
            if (legheVM.mieleghe.value == true) {
                adapter.setonclick(object : LegheAdapter.SetOnClickListener {
                    override fun onClick(position: Int, lega: Lega) {
                        val legaintent = Intent(requireActivity(), LegaActivity::class.java)
                        legaintent.putExtra("lega", lega)
                        startActivity(legaintent)
                    }
                })
            } else {
                adapter.setonclick(object : LegheAdapter.SetOnClickListener {
                    override fun onClick(position: Int, lega: Lega) {
                        legheVM.setSelectedLeague(lega)
                        legheVM.checkrichiesta(
                            parseJsonToModel(
                                SharedPreferencesManager.getString(
                                    "utente",
                                    ""
                                )
                            ), lega
                        )

                    }
                })
            }
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.adapter = adapter
        }
        legheVM.inviando.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else if (it == false) {
                binding.progressBar.visibility = View.GONE
                IscrizioneDialog.dismiss()
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("SUCCESSO")
                alertDialog.setMessage("La tua richiesta è stata correttamente inviata")
                alertDialog.setButton(
                    AlertDialog.BUTTON_POSITIVE, "OK",
                ) { dialog, which ->
                    dialog.dismiss()
                    legheVM.resetinviando()
                }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(Color.parseColor("#ff5722"))
            }
        }
        legheVM.checkrichiest.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.progressBar.visibility = View.GONE
                IscrizioneDialog.setContentView(R.layout.fragment_custom_dialog)
                IscrizioneDialog.findViewById<TextView>(R.id.dialogTitle).setText(
                    "Vuoi iscriverti alla lega ${
                        legheVM.selectedlega.value?.name
                            ?: ""
                    }?"
                )
                IscrizioneDialog.findViewById<Button>(R.id.yesButton).setOnClickListener {
                    legheVM.selectedlega.value?.let { it1 ->
                        legheVM.chiedidipartecipare(
                            parseJsonToModel(
                                SharedPreferencesManager.getString(
                                    "utente",
                                    ""
                                )
                            ), it1
                        )
                    }
                }
                IscrizioneDialog.findViewById<Button>(R.id.noButton).setOnClickListener {
                    IscrizioneDialog.dismiss()
                    legheVM.resetcheckrichiesta()
                }
                IscrizioneDialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                IscrizioneDialog.show()
            } else if (it == false) {
                binding.progressBar.visibility = View.GONE
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("ATTENZIONE")
                alertDialog.setMessage("Hai già fatto richiesta oppure sei stato invitato a questa lega")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "OK",
                ) { dialog, which ->
                    dialog.dismiss()
                    legheVM.resetcheckrichiesta()
                }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.parseColor("#ff5722"))

            }
        }
        binding.buttonMyLeagues.setOnClickListener {
            legheVM.setMieleghe(true, utente.id)
        }
        binding.buttonJoinLeague.setOnClickListener {
            legheVM.setMieleghe(false, utente.id)
        }


        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Code to execute before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
               legheVM.setLegheFiltrate(s.toString())
                //Log.d("TAG", "onTextChanged: ${s.toString()}")
            }
        }
        binding.searched.addTextChangedListener(textWatcher)

        legheVM.filtrate.observe(viewLifecycleOwner) {
            if(it!=null){
                if(it==true){
                    val adapter = legheVM.leghefiltrate.value?.let { it1 -> LegheAdapter(it1.toList(), imagesVM, false) }
                    if (legheVM.mieleghe.value == true) {
                        adapter?.setonclick(object : LegheAdapter.SetOnClickListener {
                            override fun onClick(position: Int, lega: Lega) {
                                val legaintent = Intent(requireActivity(), LegaActivity::class.java)
                                legaintent.putExtra("lega", lega)
                                startActivity(legaintent)
                            }
                        })
                    } else {
                        adapter?.setonclick(object : LegheAdapter.SetOnClickListener {
                            override fun onClick(position: Int, lega: Lega) {
                                legheVM.setSelectedLeague(lega)
                                legheVM.checkrichiesta(
                                    parseJsonToModel(
                                        SharedPreferencesManager.getString(
                                            "utente",
                                            ""
                                        )
                                    ), lega
                                )

                            }
                        })
                    }
                    binding.recyclerView.layoutManager = LinearLayoutManager(context)
                    binding.recyclerView.adapter = adapter

                }
            }
        }


    }

}