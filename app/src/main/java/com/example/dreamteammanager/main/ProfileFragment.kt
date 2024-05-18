package com.example.dreamteammanager.main

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.dreamteammanager.R
import com.example.dreamteammanager.databinding.FragmentProfileBinding
import com.example.dreamteammanager.viewmodel.SharedPreferencesManager
import com.example.dreamteammanager.viewmodel.UserViewModel
import com.example.dreamteammanager.viewmodel.parseJsonToModel

class ProfileFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val passwordDialog = Dialog(requireActivity())

        binding.ChangePasswordButton.setOnClickListener {
            passwordDialog.setContentView(R.layout.dialog_change_password)
            passwordDialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            passwordDialog.show()
        }

        binding.Username.setText(userViewModel.user.value!!.username)
        binding.Email.setText(userViewModel.user.value!!.email)


    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel.setUtente(
            parseJsonToModel(
                SharedPreferencesManager.getString(
                    "utente",
                    ""
                )
            )
        )
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}