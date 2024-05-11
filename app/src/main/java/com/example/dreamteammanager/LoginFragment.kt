package com.example.dreamteammanager

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.dreamteammanager.classi.RecuperoCredenzialiFragment
import com.example.dreamteammanager.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registratilabel.setOnClickListener {
            (context as AppCompatActivity).supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<RegisterFragment>(R.id.fragment_container)
                addToBackStack("login")
            }
        }

        binding.LoginButton.setOnClickListener{
            val MainIntent = Intent(context, MainActivity::class.java)
            (context as AppCompatActivity).startActivity(MainIntent)
        }

        binding.recuperaPassword.setOnClickListener{
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