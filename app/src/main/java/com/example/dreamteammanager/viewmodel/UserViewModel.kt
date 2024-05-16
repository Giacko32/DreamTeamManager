package com.example.dreamteammanager.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.retrofit.Client
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class UserViewModel(application: Application) : AndroidViewModel(application) {
    init {
        SharedPreferencesManager.init(application)
    }

    fun parseJsonToModel(jsonString: String): Utente {
        val gson = Gson()
        return gson.fromJson(
            jsonString,
            object : com.google.gson.reflect.TypeToken<Utente>() {}.type
        )
    }

    fun parseJsonToModel2(jsonString: String): ArrayList<Utente> {
        val gson = Gson()
        return gson.fromJson(
            jsonString,
            object : com.google.gson.reflect.TypeToken<Utente>() {}.type
        )
    }

    fun parseModelToJson(utente: Utente): String {
        val gson = Gson()
        return gson.toJson(utente)
    }


    object SharedPreferencesManager {

        private const val PREF_NAME = "MySharedPrefs"
        private lateinit var sharedPreferences: SharedPreferences

        fun init(context: Context) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }

        fun saveString(key: String, value: String) {
            sharedPreferences.edit().putString(key, value).apply()
        }

        fun getString(key: String, defaultValue: String): String {
            return sharedPreferences.getString(key, defaultValue) ?: defaultValue
        }
    }

    private val _user = MutableLiveData<Utente?>()
    val user: LiveData<Utente?>
        get() = _user

    init {
        _user.value = null
    }

    fun getUtente()
    {
        val jsonString = SharedPreferencesManager.getString("utente", "")
        if (jsonString.isNotEmpty()) {
            val utente = parseJsonToModel(jsonString)
            _flagRicordami.value = true
            _user.value = utente
        } else {
            _user.value = null
        }
    }

    fun setUtente(utente: Utente) {
        _user.value = utente
    }

    private val _flagRicordami = MutableLiveData<Boolean>()
    val flagRicordami: LiveData<Boolean>
        get() = _flagRicordami

    init {
        _flagRicordami.value = false
    }

    fun updateFlag(isChecked: Boolean) {
        _flagRicordami.value = isChecked
        Log.d("FLAG", "updateFlag: $isChecked")

    }

    fun insert(utente: Utente) {
        val jsonString = parseModelToJson(utente)
        SharedPreferencesManager.saveString("utente", jsonString)
    }
    private val _loggato=MutableLiveData<Boolean?>()
    val loggato:LiveData<Boolean?>
            get() = _loggato
    init {
        _loggato.value=null
    }


    fun failogin(username: String, password: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            Client.retrofit.getuser(username, password).enqueue(
                object : Callback<JsonObject> {
                    override fun onResponse(
                        call: Call<JsonObject>, response:
                        Response<JsonObject>
                    ) {
                        if (response.isSuccessful) {
                            val utente = response.body().toString()
                            _user.value = parseJsonToModel(utente)
                            if (user.value!!.username != null) {
                                _loggato.value=true
                            }else{
                                _loggato.value=false
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<JsonObject>?, t:
                        Throwable?
                    ) {
                        _loggato.value=false
                    }
                })


    }
    }


    fun logout(flag: Boolean) {
        if (!flag) {
            SharedPreferencesManager.saveString("utente", "")
        }
        _user.value = null
    }

    fun registrati(username: String, password: String, email: String): String {
        var disponibilita: Boolean
        var stringadiritorno = ""
        if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
            if (password.length >= 8 && password.length <= 25) {
                if (isValidEmail(email)) {
                    Client.retrofit.checkDisponibilita(username, email).enqueue(
                        object : Callback<JsonArray> {
                            override fun onResponse(
                                call: Call<JsonArray>, response:
                                Response<JsonArray>
                            ) {
                                if (response.isSuccessful) {
                                    val array = parseJsonToModel2(response.body().toString())
                                    if (array.isEmpty()) {
                                        disponibilita = true
                                    } else {
                                        disponibilita = false
                                        stringadiritorno = "Username o email già in uso"
                                    }
                                    if (disponibilita) {
                                        Client.retrofit.registeruser(username, password, email)
                                            .enqueue(
                                                object : Callback<JsonObject> {
                                                    override fun onResponse(
                                                        call: Call<JsonObject>, response:
                                                        Response<JsonObject>
                                                    ) {
                                                        if (response.isSuccessful) {
                                                            stringadiritorno =
                                                                "Registrazione avvenuta con successo"
                                                        } else {
                                                            stringadiritorno =
                                                                "Errore di connessione"
                                                        }
                                                    }

                                                    override fun onFailure(
                                                        call: Call<JsonObject>?, t:
                                                        Throwable?
                                                    ) {
                                                        stringadiritorno = "Errore di connessione"
                                                    }
                                                })
                                    }
                                } else {
                                    stringadiritorno = "Errore di connessione"
                                }
                            }

                            override fun onFailure(
                                call: Call<JsonArray>?, t:
                                Throwable?
                            ) {
                                stringadiritorno = "Errore di connessione"
                            }

                        }
                    )
                } else {
                    stringadiritorno = "Email non valida"
                }
            } else {
                stringadiritorno = "Password non formattata correttamente"
            }
        } else {
            stringadiritorno = "Campi lasciati vuoti"
        }
        return stringadiritorno
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
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

    fun recuperaCredenziali(email: String): Boolean {
        if (email.isNotEmpty() && isValidEmail(email)) {
            generatecodice()
            Log.d("CODICE", "recuperaCredenziali: ${codice.value} ed email inviata")
            //sendEmail(email,getApplication())
            return true
        } else {
            Log.d("CODICE", "recuperaCredenziali: email non valida")
            return false
        }
    }

    fun controllacodice(codice: Int): Boolean {
        if (codice == this.codice.value) {
            return true
        }
        return false
    }

    fun cambiapassword(password: String, confirm: String): Boolean {
        if (password.isNotEmpty() && password.length >= 8 && password.length <= 25) {
            if (password == confirm) {
                //Cambia password in DB remoto
                return true
            }
        }
        return false
    }

}
