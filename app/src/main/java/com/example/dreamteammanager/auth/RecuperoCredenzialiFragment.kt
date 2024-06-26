package com.example.dreamteammanager.auth

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.savedstate.R
import com.example.dreamteammanager.databinding.FragmentRecuperoCredenzialiBinding
import com.example.dreamteammanager.viewmodel.RecoveryViewModel
import com.example.dreamteammanager.viewmodel.UserViewModel


class RecuperoCredenzialiFragment : Fragment() {
    lateinit var binding: FragmentRecuperoCredenzialiBinding
    private val recoveryViewModel:RecoveryViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecuperoCredenzialiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recoveryViewModel.mailverificata.observe(viewLifecycleOwner){
            if(it=="Mail associata"){
                binding.progressBar.visibility=View.GONE
                binding.codicefield.visibility = View.VISIBLE
                binding.mailfield.visibility = View.GONE
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("SUCCESSO")
                alertDialog.setMessage("Il codice è stato inviato con successo all'indirizzo email inserito")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "VAI AVANTI"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.parseColor("#ff5722"))
                binding.actionButton.text = "Conferma Codice"
            }else if(it=="Mail non associata"){
                binding.progressBar.visibility=View.GONE
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("ATTENZIONE")
                alertDialog.setMessage("Email inserita non valida")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "RIPROVA"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#ff5722"))
            }else if(it=="Verificando"){
                binding.progressBar.visibility=View.VISIBLE
            }
        }

        binding.actionButton.setOnClickListener {
            if (binding.codicefield.visibility == View.GONE) {
                recoveryViewModel.verificamail(binding.EmailEditText.text.toString())
            }else{
                if(recoveryViewModel.controllacodice(binding.codeEditText.text.toString().toInt())){
                    parentFragmentManager.commit {
                        setReorderingAllowed(true)
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        replace<CambioPasswordFragment>(com.example.dreamteammanager.R.id.fragment_container)
                        addToBackStack("go-on")
                    }
                }else{
                        val alertDialog = AlertDialog.Builder(
                            requireContext(),
                            androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                        ).create()
                        alertDialog.setTitle("ATTENZIONE")
                        alertDialog.setMessage("Codice inserito non corretto")
                        alertDialog.setButton(
                            AlertDialog.BUTTON_NEGATIVE, "RIPROVA"
                        ) { dialog, which -> dialog.dismiss() }
                        alertDialog.show()
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#ff5722"))
                    }

            }


            }
        }
    }




