package com.example.dreamteammanager.main

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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


class ProfileFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    lateinit var binding: FragmentProfileBinding

    private val pickPhoto =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) {
            if (it.firstOrNull() != null) {
                val contentResolver = requireContext().contentResolver
                val inputStream = contentResolver.openInputStream(it.first())
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
                val newWidth: Float = 500.0F
                var newHeight: Float = 500.0F
                newHeight = newWidth / aspectRatio
                val finalBitmap =
                    Bitmap.createScaledBitmap(bitmap, newWidth.toInt(), newHeight.toInt(), true)
                val bos = ByteArrayOutputStream()
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
                val toload = bos.toByteArray()
                val encodedImage = Base64.encodeToString(toload, Base64.NO_WRAP)
                val utente = parseJsonToModel(SharedPreferencesManager.getString("utente", ""))
                val profimg = ProfileImage(utente.id, encodedImage)
                val gson =
                    JsonParser.parseString(parseModelToJson(profimg))
                Log.d("ProfileFragment", gson.asJsonObject.toString())
                Client.retrofit.insertImage(gson.asJsonObject).enqueue(
                    object : Callback<JsonObject> {
                        override fun onResponse(
                            call: Call<JsonObject>, response:
                            Response<JsonObject>
                        ) {
                            if (response.isSuccessful) {
                                binding.userimage.setImageBitmap(finalBitmap)
                            }
                        }

                        override fun onFailure(
                            call: Call<JsonObject>?, t:
                            Throwable?
                        ) {
                            binding.userimage.setImageResource(R.drawable.baseline_account_circle_24)
                        }

                    })
            } else {
                binding.userimage.setImageResource(R.drawable.baseline_account_circle_24)
            }
        }

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

        binding.userimage.setOnClickListener {
            pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.Username.setText(userViewModel.user.value!!.username)
        binding.Email.setText(userViewModel.user.value!!.email)

        binding.ModifyButton.setOnClickListener{
            val username = binding.Username.text.toString()
            val email = binding.Email.text.toString()
            userViewModel.modificaCredenzialiProfilo(email, username, userViewModel.user.value!!.id)

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

class ProfileImage(val userid: Int, val image: String)