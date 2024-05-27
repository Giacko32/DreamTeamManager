package com.example.dreamteammanager.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.dreamteammanager.main.MainActivity
import com.example.dreamteammanager.R
import com.example.dreamteammanager.databinding.FragmentLoginBinding
import com.example.dreamteammanager.viewmodel.SharedPreferencesManager
import com.example.dreamteammanager.viewmodel.UserViewModel


class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    private val userviewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userviewModel.getUtente()
        val progressBar =
            (context as AppCompatActivity).findViewById<ProgressBar>(R.id.progress_bar)

        if (SharedPreferencesManager.getString("flagRicordami", "") == "true") {
            userviewModel.failogin(
                userviewModel.user.value!!.username,
                userviewModel.user.value!!.password
            )
        }

        userviewModel.loggato.observe(requireActivity()) { login ->
            if (login == "loggato") {
                progressBar.visibility = View.GONE
                val MainIntent = Intent(context, MainActivity::class.java)
                MainIntent.putExtra("user", userviewModel.user.value)
                MainIntent.putExtra("flag", userviewModel.flagRicordami.value)
                (context as AppCompatActivity).startActivity(MainIntent)
                (context as AppCompatActivity).finish()
            }
            if (login == "non loggato") {
                progressBar.visibility = View.GONE
            }
            if (login == "errore") {
                progressBar.visibility = View.GONE
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
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.parseColor("#ff5722"))
                userviewModel.resetloggato()
            }
            if (login == "accesso in corso") {
                progressBar.visibility = View.VISIBLE
            }
        }

        binding.remembermebox.setOnCheckedChangeListener { buttonView, isChecked ->
            userviewModel.updateFlag(isChecked)
        }

        binding.registratilabel.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                replace<RegisterFragment>(R.id.fragment_container)
                addToBackStack("registrati")
            }
        }


        binding.LoginButton.setOnClickListener {
            val username = binding.usernametext.text.toString().trimEnd()
            val password = binding.passwordtext.text.toString().trimEnd()
            userviewModel.failogin(username, password)
        }

        binding.recuperaPassword.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
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
