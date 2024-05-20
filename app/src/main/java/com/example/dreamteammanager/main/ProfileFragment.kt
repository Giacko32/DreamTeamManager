package com.example.dreamteammanager.main

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.RegistraUtente
import com.example.dreamteammanager.databinding.FragmentProfileBinding
import com.example.dreamteammanager.retrofit.Client
import com.example.dreamteammanager.retrofit.UserAPI
import com.example.dreamteammanager.viewmodel.SharedPreferencesManager
import com.example.dreamteammanager.viewmodel.UserViewModel
import com.example.dreamteammanager.viewmodel.parseJsonToModel
import com.example.dreamteammanager.viewmodel.parseModelToJson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import android.util.Base64
import androidx.appcompat.app.AlertDialog
import com.example.dreamteammanager.viewmodel.ProfileImageVM


class ProfileFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()
    private val profimgViewModel: ProfileImageVM by viewModels()
    lateinit var binding: FragmentProfileBinding

    private val pickPhoto =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) {
            if (it.firstOrNull() != null) {
                profimgViewModel.uploadProfileImage(requireContext(), it.first())
            } else {
                if(profimgViewModel.image.value != null) {
                    binding.userimage.setImageBitmap(profimgViewModel.image.value)
                } else {
                    binding.userimage.setImageResource(R.drawable.baseline_account_circle_24)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profimgViewModel.image.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.userimage.setImageBitmap(profimgViewModel.image.value)
            }
        }

        profimgViewModel.getProfileImage(
            userViewModel.user.value
            !!.id
        )
        val passwordDialog = Dialog(requireActivity())

        binding.ChangePasswordButton.setOnClickListener {
            passwordDialog.setContentView(R.layout.dialog_change_password)
            passwordDialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            passwordDialog.show()
        }

        binding.userimage.setOnClickListener {
            pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.Username.setText(userViewModel.user.value!!.username)
        binding.Email.setText(userViewModel.user.value!!.email)

        binding.ModifyButton.setOnClickListener{
            val username = binding.Username.text.toString()
            val email = binding.Email.text.toString()
            userViewModel.checkModifica(email, username)

        }
        userViewModel.disponibilitàModifica.observe(requireActivity()){
            if( it == true){
                val username = binding.Username.text.toString()
                val email = binding.Email.text.toString()
                userViewModel.modificaCredenzialiProfilo(email,username, userViewModel.user.value!!.id)
            }else if(it == false){
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("ATTENZIONE")
                alertDialog.setMessage("Le credenziali non sono disponibili")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.parseColor("#ff5722"))
            }
        }

        userViewModel.stringaCredenziali.observe(requireActivity()){
            if( it == "Credenziali aggiornate" ){
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("Successo")
                alertDialog.setMessage("Le credenziali sono state cambiate")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.parseColor("#ff5722"))

            }

        }
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

