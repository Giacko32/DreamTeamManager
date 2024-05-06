package com.example.dreamteammanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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




}