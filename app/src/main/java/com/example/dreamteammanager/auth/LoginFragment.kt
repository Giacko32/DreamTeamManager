package com.example.dreamteammanager.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.dreamteammanager.main.MainActivity
import com.example.dreamteammanager.R
import com.example.dreamteammanager.databinding.FragmentLoginBinding
import com.example.dreamteammanager.viewmodel.UserViewModel


class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    private val userviewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.remembermebox.setOnCheckedChangeListener { buttonView, isChecked ->
            userviewModel.updateFlag(isChecked)
        }

        binding.registratilabel.setOnClickListener {
            (context as AppCompatActivity).supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<RegisterFragment>(R.id.fragment_container)
                addToBackStack("login")
            }
        }

        binding.LoginButton.setOnClickListener {
            val username = binding.usernametext.text.toString()
            val password = binding.passwordtext.text.toString()
            val login = userviewModel.failogin(username, password)
            if (login) {
                val MainIntent = Intent(context, MainActivity::class.java)
                (context as AppCompatActivity).startActivity(MainIntent)
            } else {
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("Attenzione")
                alertDialog.setMessage("Le credenziali sono errate o i campi sono vuoti")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "RIPROVA"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#ff5722"))

            }
        }

        binding.recuperaPassword.setOnClickListener {
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            fragmentManager.commit {
                setReorderingAllowed(true)
                replace<RecuperoCredenzialiFragment>(R.id.fragment_container)
                addToBackStack("login_to_recupero")
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}