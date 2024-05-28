package com.example.dreamteammanager.auth

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.dreamteammanager.R
import com.example.dreamteammanager.databinding.FragmentRegisterBinding
import com.example.dreamteammanager.viewmodel.UserViewModel


class RegisterFragment : Fragment() {
    lateinit var bind: FragmentRegisterBinding
    private val userviewModel: UserViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return bind.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind.ButtonRegistrati.setOnClickListener {
            val username = bind.UsernameText.text.toString().trimEnd()
            val password = bind.PasswordText.text.toString().trimEnd()
            val email = bind.EmailText.text.toString().trimEnd()
            bind.progressBar.visibility = View.VISIBLE
            userviewModel.checkdisponibilita(username,password,email)
        }

        userviewModel.disponibilita.observe(requireActivity()){
            val username = bind.UsernameText.text.toString().trimEnd()
            val password = bind.PasswordText.text.toString().trimEnd()
            val email = bind.EmailText.text.toString().trimEnd()
            if (it == true){
                userviewModel.registrazione(username,password,email)
            }else if(it==false){
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("ATTENZIONE")
                alertDialog.setMessage("Credenziali già registrate")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "RIPROVA",
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#ff5722"))
            }
        }

        userviewModel.stringadiritorno.observe(requireActivity()){
            if(it=="Registrazione effettuata"){
                bind.progressBar.visibility = View.GONE
                parentFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<LoginFragment>(R.id.fragment_container)
                }
            }else if(it!=null){
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("ATTENZIONE")
                alertDialog.setMessage(it)
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "RIPROVA",
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#ff5722"))
            }
        }

        bind.LoginText.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                replace<LoginFragment>(R.id.fragment_container)
                addToBackStack("back")
            }
        }
    }

}
