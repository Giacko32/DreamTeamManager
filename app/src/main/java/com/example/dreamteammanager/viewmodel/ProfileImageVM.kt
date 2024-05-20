package com.example.dreamteammanager.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.util.Base64
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.retrofit.Client
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class ProfileImageVM : ViewModel() {

    private val _image = MutableLiveData<Bitmap?>()
    val image: LiveData<Bitmap?>
        get() = _image

    init {
        _image.value = null
    }

    public fun uploadProfileImage(context: Context, uri: Uri) {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
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
        Client.retrofit.insertImage(gson.asJsonObject).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response:
                    Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        _image.value = finalBitmap
                        val alertDialog = AlertDialog.Builder(
                            context,
                            androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                        ).create()
                        alertDialog.setTitle("SUCCESSO")
                        alertDialog.setMessage("La foto profilo è stata correttamente aggiornata")
                        alertDialog.setButton(
                            AlertDialog.BUTTON_POSITIVE, "OK",
                        ) { dialog, which ->
                            dialog.dismiss()
                        }
                        alertDialog.show()
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setTextColor(Color.parseColor("#ff5722"))
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject>?, t:
                    Throwable?
                ) {
                    val alertDialog = AlertDialog.Builder(
                        context,
                        androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
                    ).create()
                    alertDialog.setTitle("SUCCESSO")
                    alertDialog.setMessage("Errore nel caricamento delle immagini")
                    alertDialog.setButton(
                        AlertDialog.BUTTON_POSITIVE, "OK",
                    ) { dialog, which ->
                        dialog.dismiss()
                    }
                    alertDialog.show()
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(Color.parseColor("#ff5722"))

                }
            })
    }

    public fun getProfileImage(userId: Int) {
        Client.retrofit.getProfileImage(userId).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response:
                    Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val jsonObject = response.body()
                        val encodedImage = jsonObject?.get("image")?.asString
                        if (encodedImage != null) {
                            val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
                            _image.value =
                                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject>?, t:
                    Throwable?
                ) {
                    _image.value = null
                }
            })
    }

}

class ProfileImage(val userid: Int, val image: String)