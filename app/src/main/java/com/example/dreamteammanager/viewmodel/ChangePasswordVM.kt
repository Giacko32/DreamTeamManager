package com.example.dreamteammanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.retrofit.Client
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordVM:ViewModel() {
    private val _passwordmodified= MutableLiveData<String?>()
    val passwordmodified: LiveData<String?>
        get()=_passwordmodified
    init {
        _passwordmodified.value=null
    }

    fun cambiapassword(password: String, confirm: String){
        if (password.isNotEmpty() && password.length >= 8 && password.length <= 25) {
            if (password == confirm) {
                _passwordmodified.value = "Modificando"
                val gson=JsonParser.parseString(SharedPreferencesManager.getString("user",""))
                Client.retrofit.cambiapassword(gson.asJsonObject).enqueue(
                    object : Callback<JsonObject> {
                        override fun onResponse(
                            call: Call<JsonObject>, response:
                            Response<JsonObject>
                        ) {
                            if (response.isSuccessful) {
                                _passwordmodified.value="Modificato"
                            }else{
                                _passwordmodified.value="Errore nel cambio password"
                            }
                        }

                        override fun onFailure(
                            call: Call<JsonObject>?, t:
                            Throwable?
                        ) {
                            _passwordmodified.value="Errore nel cambio password"
                        }

                    }
                )
            } else {
                _passwordmodified.value = "Le password non corrispondono"
            }
        }else{
            _passwordmodified.value="La nuova password non rispetta i vincoli"
        }

    }
}
