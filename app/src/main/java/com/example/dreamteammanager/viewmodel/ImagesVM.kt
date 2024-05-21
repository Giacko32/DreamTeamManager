package com.example.dreamteammanager.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.util.Base64
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.dreamteammanager.R
import com.example.dreamteammanager.retrofit.Client
import com.example.dreamteammanager.retrofit.UserAPI
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class ImagesVM : ViewModel() {


    private val _changed = MutableLiveData<Boolean?>()
    val changed: LiveData<Boolean?>
        get() = _changed

    init {
        _changed.value = false
    }

    public fun BitmaptoBase64(context: Context, uri: Uri, size: Int): String {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val newWidth: Float = size.toFloat()
        var newHeight: Float = size.toFloat()
        newHeight = newWidth / aspectRatio
        val finalBitmap =
            Bitmap.createScaledBitmap(bitmap, newWidth.toInt(), newHeight.toInt(), true)
        val bos = ByteArrayOutputStream()
        finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        return Base64.encodeToString(bos.toByteArray(), Base64.NO_WRAP)
    }


    public fun uploadProfileImage(context: Context, uri: Uri) {
        val utente = parseJsonToModel(SharedPreferencesManager.getString("utente", ""))
        val profimg = ProfileImage(utente.id, BitmaptoBase64(context, uri, 600))
        val gson =
            JsonParser.parseString(parseModelToJson(profimg))
        Client.retrofit.insertImage(gson.asJsonObject).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response:
                    Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        _changed.value = true
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

    public fun getProfilePic(context: Context, id: Int, place: ImageView) {
        Glide.with(context)
            .load("${UserAPI.BASE_URL}/pwm/img/img${id}.jpg")
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.baseline_account_circle_24) // Placeholder image
                    .error(R.drawable.baseline_account_circle_24) // Error image in case of loading failure
            ).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
            .into(place)
    }


    public fun getLegaImage(context: Context, img: Int, place: ImageView)
    {
        Glide.with(context)
            .load("${UserAPI.BASE_URL}/pwm/img/lega${img}.jpeg")
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.baseline_account_circle_24) // Placeholder image
                    .error(R.drawable.baseline_account_circle_24) // Error image in case of loading failure
            ).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
            .into(place)
    }

}

class ProfileImage(val userid: Int, val image: String)