package com.example.dreamteammanager.viewmodel

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dreamteammanager.classi.Emailcode
import com.example.dreamteammanager.retrofit.Client
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecoveryViewModel(app: Application) : AndroidViewModel(app) {
    init {
        SharedPreferencesManager.init(app)
    }

    fun parseModelToJson(utente: Any): String {
        val gson = Gson()
        return gson.toJson(utente)
    }

    private val _codice = MutableLiveData<Int>()
    val codice: LiveData<Int>
        get() = _codice

    init {
        _codice.value = 0
    }

    private fun generatecodice() {
        _codice.value = (100000..999999).random()
    }

    private val _mailverificata = MutableLiveData<String?>()
    val mailverificata: LiveData<String?>
        get() = _mailverificata

    init {
        _mailverificata.value = ""
    }

    fun verificamail(email: String) {
        if (email.isNotEmpty() && isValidEmail(email)) {
            _mailverificata.value = "Verificando"
            Client.retrofit.verificaemail(email).enqueue(
                object : Callback<JsonObject> {
                    override fun onResponse(
                        call: Call<JsonObject>, response:
                        Response<JsonObject>
                    ) {
                        if (response.isSuccessful) {
                            val utente = response.body().toString()
                            if (utente != "{}") {
                                _mailverificata.value = "Mail associata"
                                SharedPreferencesManager.saveString("user", utente)
                                inviamail(email)
                            } else {
                                _mailverificata.value = "Mail non associata"
                            }

                        }
                    }

                    override fun onFailure(
                        call: Call<JsonObject>?, t:
                        Throwable?
                    ) {
                        _mailverificata.value = "Mail non associata"

                    }

                }
            )


        } else {
            _mailverificata.value = "Mail non associata"
        }

    }

    fun inviamail(email: String) {
        generatecodice()
        val gson = JsonParser.parseString(parseModelToJson(Emailcode(email, codice.value!!)))
        Client.retrofit.inviamail(Emailcode(email, codice.value!!)).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response:
                    Response<JsonObject>
                ) {
                    if (response.isSuccessful) {

                    }
                }

                override fun onFailure(
                    call: Call<JsonObject>?, t:
                    Throwable?
                ) {

                }

            }
        )

    }

    fun controllacodice(codice: Int): Boolean {
        if (codice == this.codice.value) {
            return true
        }
        return false
    }


    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}
