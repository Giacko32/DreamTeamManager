package com.example.dreamteammanager.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.dreamteammanager.R
import com.example.dreamteammanager.databinding.FragmentProfileBinding
import com.example.dreamteammanager.viewmodel.UserViewModel

class ProfileFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()
    lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.user.observe(viewLifecycleOwner, Observer{
        if (it != null) {
            binding.Username.setText(it.username)
            binding.Email.setText(it.email)
        }

        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}