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
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.viewmodel.ImagesVM

import com.google.android.material.textfield.TextInputEditText


class ProfileFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()
    private val profimgViewModel: ImagesVM by viewModels()
    lateinit var binding: FragmentProfileBinding


    private val pickPhoto =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) {
            if (it.firstOrNull() != null) {
                profimgViewModel.uploadProfileImage(requireContext(), it.first())
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var newpassword:String=""

        profimgViewModel.changed.observe(viewLifecycleOwner) {
            profimgViewModel.getProfilePic(
                requireContext(),
                userViewModel.user.value!!.id,
                binding.userimage, false
            )
        }

        val passwordDialog = Dialog(requireActivity())

        binding.ChangePasswordButton.setOnClickListener {
            passwordDialog.setContentView(R.layout.dialog_change_password)
            passwordDialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            passwordDialog.findViewById<Button>(R.id.ChangeButton).setOnClickListener {
                binding.progressBar.visibility=View.VISIBLE
                val new_password =
                    passwordDialog.findViewById<TextInputEditText>(R.id.newpasswordfield).text.toString()
                val old_password =
                    passwordDialog.findViewById<TextInputEditText>(R.id.oldpasswordfield).text.toString()
                if (old_password == userViewModel.user.value!!.password && new_password.length in  1..25) {
                    newpassword=new_password
                    userViewModel.changeProfilePassword(
                        new_password,
                        userViewModel.user.value!!.email
                    )
                    passwordDialog.dismiss()

                } else {
                    val alertDialog = AlertDialog.Builder(
                        requireContext(),
                        androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                    ).create()
                    alertDialog.setTitle("ATTENZIONE")
                    alertDialog.setMessage("La vecchia password inserita non corrisponde")
                    alertDialog.setButton(
                        AlertDialog.BUTTON_NEGATIVE, "RIPROVA"
                    ) { dialog, which -> dialog.dismiss() }
                    alertDialog.show()
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(Color.parseColor("#ff5722"))
                }
            }
            passwordDialog.show()


        }

        userViewModel.modificaPassword.observe(requireActivity()) {
            if (it == true) {
                val utente=Utente(1,userViewModel.user.value!!.username,
                    newpassword,
                    userViewModel.user.value!!.email)
                userViewModel.setUtente(utente)
                SharedPreferencesManager.saveString("utente", parseModelToJson(utente))
                binding.progressBar.visibility=View.GONE
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("SUCCESSO")
                alertDialog.setMessage("Password modificata")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.parseColor("#ff5722"))

                passwordDialog.dismiss()
            } else if (it == false) {
                binding.progressBar.visibility=View.GONE
                val alertDialog = AlertDialog.Builder(
                    requireContext(),
                    androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                ).create()
                alertDialog.setTitle("ATTENZIONE")
                alertDialog.setMessage("Si è verificato un errore nel cambio password")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "RIPROVA"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.parseColor("#ff5722"))
                passwordDialog.dismiss()
            }
        }

        binding.userimage.setOnClickListener {
            pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.Username.setText(userViewModel.user.value!!.username)
        binding.Email.setText(userViewModel.user.value!!.email)

        binding.ModifyButton.setOnClickListener {
            binding.progressBar.visibility=View.VISIBLE
            val username = binding.Username.text.toString()
            val email = binding.Email.text.toString()
            if (username == userViewModel.user.value!!.username) {
                if (email == userViewModel.user.value!!.email) {

                } else {
                    userViewModel.checkModifica(email, "null")
                }
            }

            if (email == userViewModel.user.value!!.email) {
                if (username == userViewModel.user.value!!.username) {
                    val alertDialog = AlertDialog.Builder(
                        requireContext(),
                        androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                    ).create()
                    alertDialog.setTitle("ATTENZIONE")
                    alertDialog.setMessage("Le credenziali sono uguali a quelle attuali")
                    alertDialog.setButton(
                        AlertDialog.BUTTON_NEGATIVE, "OK"
                    ) { dialog, which -> dialog.dismiss() }
                    alertDialog.show()
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(Color.parseColor("#ff5722"))
                    binding.progressBar.visibility=View.GONE
                } else {
                    userViewModel.checkModifica("null", username)
                }
            } else {
                userViewModel.checkModifica(email, username)
            }


        }
        userViewModel.disponibilitàModifica.observe(requireActivity()) {
            if (it == true) {
                val username = binding.Username.text.toString().trimEnd()
                val email = binding.Email.text.toString().trimEnd()
                userViewModel.modificaCredenzialiProfilo(
                    email,
                    username,
                    userViewModel.user.value!!.id
                )
            } else if (it == false) {
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
                binding.progressBar.visibility=View.GONE
            }
        }

        userViewModel.stringaCredenziali.observe(requireActivity()) {
            if (it == "Credenziali aggiornate") {
                val username = binding.Username.text.toString().trimEnd()
                val email = binding.Email.text.toString().trimEnd()
                val utente=Utente(1,username,userViewModel.user.value!!.password,email)
                userViewModel.setUtente(utente)
                SharedPreferencesManager.saveString("utente", parseModelToJson(utente))
                binding.progressBar.visibility=View.GONE
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

