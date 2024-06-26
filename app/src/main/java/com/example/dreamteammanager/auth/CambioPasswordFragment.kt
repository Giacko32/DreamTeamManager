package com.example.dreamteammanager.auth

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.dreamteammanager.R
import com.example.dreamteammanager.databinding.FragmentCambioPasswordBinding
import com.example.dreamteammanager.databinding.FragmentRecuperoCredenzialiBinding
import com.example.dreamteammanager.viewmodel.ChangePasswordVM
import com.example.dreamteammanager.viewmodel.UserViewModel

class CambioPasswordFragment : Fragment() {
    lateinit var binding: FragmentCambioPasswordBinding
    private val changePasswordVM: ChangePasswordVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCambioPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.confermaButton.setOnClickListener {
            val nuovaPassword = binding.NuovaPassword.text.toString()
            val confermaPassword = binding.ConfermaPassword.text.toString()
            changePasswordVM.cambiapassword(nuovaPassword, confermaPassword)
        }

        changePasswordVM.passwordmodified.observe(viewLifecycleOwner) {
            if (it == "Modificato") {
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("SUCCESSO")
                alertDialog.setMessage("Password cambiata con successo")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "VAI AL LOGIN"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.parseColor("#ff5722"))
                parentFragmentManager.commit {
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    setReorderingAllowed(true)
                    replace<LoginFragment>(R.id.fragment_container)
                }
            } else if (it == "Modificando") {
                binding.progressBar.visibility = View.VISIBLE
            } else if(it !=null) {
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("ATTENZIONE")
                alertDialog.setMessage(it)
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "RIPROVA"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.parseColor("#ff5722"))

            }

        }
    }
}

