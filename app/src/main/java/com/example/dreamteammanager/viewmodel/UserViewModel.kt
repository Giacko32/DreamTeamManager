package com.example.dreamteammanager.viewmodel

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.util.Patterns
import android.view.ViewGroup
import android.widget.MultiAutoCompleteTextView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dreamteammanager.R
import com.example.dreamteammanager.auth.AccessActivity
import com.example.dreamteammanager.auth.LoginFragment
import com.example.dreamteammanager.classi.RegistraUtente
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.retrofit.Client
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
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

    fun parseModelToJson(utente: Any): String {
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
            _loggato.value = "loggato"
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
    private val _loggato=MutableLiveData<String?>()
    val loggato:LiveData<String?>
            get() = _loggato
    init {
        _loggato.value=null
    }


    fun failogin(username: String, password: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            _loggato.value="accesso in corso"
            Client.retrofit.getuser(username, password).enqueue(
                object : Callback<JsonObject> {
                    override fun onResponse(
                        call: Call<JsonObject>, response:
                        Response<JsonObject>
                    ) {
                        if (response.isSuccessful) {
                            val utente = response.body().toString()
                            _user.value = parseJsonToModel(utente)
                            Log.d("USER", "onResponse: ${user.value}")
                            if (user.value!!.username != null) {
                                _loggato.value="loggato"
                                if(flagRicordami.value!!){
                                    insert(user.value!!)
                                }
                            }else{
                                _loggato.value="non loggato"
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<JsonObject>?, t:
                        Throwable?
                    ) {
                        _loggato.value="non loggato"
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
    private val _stringadiritorno = MutableLiveData<String?>()
    val stringadiritorno: LiveData<String?>
        get() = _stringadiritorno
    init {
        _stringadiritorno.value=null
    }
    private val _disponibilita=MutableLiveData<Boolean?>()
    val disponibilita:LiveData<Boolean?>
        get()=_disponibilita
    init {
        _disponibilita.value=null
    }

    fun checkdisponibilita(username: String, password: String, email: String){
        if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
            if (password.length in 1..25) {
                if (isValidEmail(email)) {
                    Log.d("PRIMA","sto per andare")
                    Client.retrofit.checkDisponibilita(username, email).enqueue(
                        object : Callback<JsonArray> {
                            override fun onResponse(
                                call: Call<JsonArray>, response:
                                Response<JsonArray>
                            ) {
                                if (response.isSuccessful) {
                                    Log.d("DISP PRIMA",_disponibilita.value.toString())
                                    _disponibilita.value = response.body().isEmpty
                                    Log.d("DISP DOPO",_disponibilita.value.toString())

                                }
                            }

                            override fun onFailure(
                                call: Call<JsonArray>?, t:
                                Throwable?
                            ) {
                                _stringadiritorno.value = "Errore di connessione"
                            }

                        }
                    )
                } else {
                    _stringadiritorno.value = "Email non valida"
                }
            } else {
                _stringadiritorno.value = "Password non formattata correttamente"
            }
        } else {
            _stringadiritorno.value = "Campi lasciati vuoti"
        }

    }
    fun registrazione(username: String,password: String,email: String){
        val gson=JsonParser.parseString(parseModelToJson(RegistraUtente(username,password,email)))
        Client.retrofit.registeruser(gson.asJsonObject).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response:
                    Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        _stringadiritorno.value="Registrazione effettuata"
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject>?, t:
                    Throwable?
                ) {
                    _stringadiritorno.value = "Errore di connessione"

                }

            }
        )
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

    fun recuperaCredenziali(email: String){
        if (email.isNotEmpty() && isValidEmail(email)) {
            generatecodice()
            //Log.d("CODICE", "recuperaCredenziali: ${codice.value} ed email inviata")
            val gson=JsonParser.parseString(parseModelToJson(Emailcode(email,codice.value!!)))
            Log.d("CODICE", "recuperaCredenziali: $gson")
            Client.retrofit.inviamail(Emailcode(email,codice.value!!)).enqueue(
                object : Callback<JsonObject> {
                    override fun onResponse(
                        call: Call<JsonObject>, response:
                        Response<JsonObject>
                    ) {
                        if (response.isSuccessful) {
                            //_stringadiritorno.value="Registrazione effettuata"
                        }
                    }

                    override fun onFailure(
                        call: Call<JsonObject>?, t:
                        Throwable?
                    ) {
                        //_stringadiritorno.value = "Errore di connessione"

                    }

                }
            )

        } else {
            Log.d("CODICE", "recuperaCredenziali: email non valida")
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
class Emailcode(val email: String,val codice: Int)
