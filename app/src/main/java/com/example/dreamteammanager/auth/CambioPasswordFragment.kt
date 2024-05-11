package com.example.dreamteammanager.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dreamteammanager.R
import com.example.dreamteammanager.databinding.FragmentCambioPasswordBinding
import com.example.dreamteammanager.databinding.FragmentRecuperoCredenzialiBinding

class CambioPasswordFragment : Fragment() {
    lateinit var binding: FragmentCambioPasswordBinding

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
    }

}