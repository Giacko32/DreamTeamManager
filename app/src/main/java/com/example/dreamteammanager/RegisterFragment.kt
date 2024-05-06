package com.example.dreamteammanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.dreamteammanager.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {
    lateinit var bind: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind=FragmentRegisterBinding.inflate(layoutInflater,container,false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.ButtonRegistrati.setOnClickListener {}
        bind.LoginText.setOnClickListener{
            (context as AppCompatActivity).supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<LoginFragment>(R.id.fragment_container)
                addToBackStack("back")
            }
        }
    }




}