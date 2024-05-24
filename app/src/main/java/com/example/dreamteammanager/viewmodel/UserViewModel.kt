package com.example.dreamteammanager.viewmodel

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.ViewGroup
import android.widget.MultiAutoCompleteTextView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.dreamteammanager.R
import com.example.dreamteammanager.auth.AccessActivity
import com.example.dreamteammanager.auth.LoginFragment
import com.example.dreamteammanager.classi.Lega
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
import java.io.ByteArrayOutputStream


class UserViewModel(application: Application) : AndroidViewModel(application) {
    init {
        SharedPreferencesManager.init(application)
    }

    private val _user = MutableLiveData<Utente?>()
    val user: LiveData<Utente?>
        get() = _user

    init {
        _user.value = null
    }

    fun getUtente() {
        val ricordami = SharedPreferencesManager.getString("flagRicordami", "")
        val utentepreso = SharedPreferencesManager.getString("utente", "")
        if (ricordami != "false" && utentepreso.isNotEmpty()) {
            Log.d("GETTATO", utentepreso)
            val utente = parseJsonToModel(utentepreso)
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
        SharedPreferencesManager.saveString("flagRicordami", flagRicordami.value.toString())
    }

    private val _loggato = MutableLiveData<String?>()
    val loggato: LiveData<String?>
        get() = _loggato

    init {
        _loggato.value = null
    }

    public fun resetloggato() {
        _loggato.value = "non loggato"
    }


    fun failogin(username: String, password: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            _loggato.value = "accesso in corso"
            Client.retrofit.getuser(username, password).enqueue(
                object : Callback<JsonObject> {
                    override fun onResponse(
                        call: Call<JsonObject>, response:
                        Response<JsonObject>
                    ) {
                        if (response.isSuccessful) {
                            val utente = response.body().toString()
                            Log.d("USER", "onResponse: ${utente}")
                            if (utente != "{}") {
                                _user.value = parseJsonToModel(utente)
                                _loggato.value = "loggato"
                                insert(user.value!!)
                            } else {
                                _loggato.value = "errore"
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<JsonObject>?, t:
                        Throwable?
                    ) {
                        _loggato.value = "errore"
                    }
                })
        } else {
            _loggato.value = "errore"
        }
    }


    fun logout() {
        SharedPreferencesManager.saveString("flagRicordami", "false")
        //_user.value = null
    }

    private val _stringadiritorno = MutableLiveData<String?>()
    val stringadiritorno: LiveData<String?>
        get() = _stringadiritorno

    init {
        _stringadiritorno.value = null
    }

    private val _disponibilita = MutableLiveData<Boolean?>()
    val disponibilita: LiveData<Boolean?>
        get() = _disponibilita

    init {
        _disponibilita.value = null
    }

    fun checkdisponibilita(username: String, password: String, email: String) {
        if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
            if (password.length in 2..25) {
                if (isValidEmail(email)) {
                    Client.retrofit.checkDisponibilita(username, email).enqueue(
                        object : Callback<JsonArray> {
                            override fun onResponse(
                                call: Call<JsonArray>, response:
                                Response<JsonArray>
                            ) {
                                if (response.isSuccessful) {
                                    _disponibilita.value = response.body().isEmpty
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

    fun registrazione(username: String, password: String, email: String) {
        val gson =
            JsonParser.parseString(parseModelToJson(RegistraUtente(username, password, email)))
        Client.retrofit.registeruser(gson.asJsonObject).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response:
                    Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        _stringadiritorno.value = "Registrazione effettuata"
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

    private val _stringaCredenziali = MutableLiveData<String?>()
    val stringaCredenziali: LiveData<String?>
        get() = _stringaCredenziali

    init {
        _stringaCredenziali.value = null
    }

    fun modificaCredenzialiProfilo(email: String, username: String, id: Int) {
        val gson =
            JsonParser.parseString(parseModelToJson(ModifyCredenzialiProfile(email, username, id)))
        Client.retrofit.changeCredenzialiProfile(gson.asJsonObject).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response:
                    Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        _stringaCredenziali.value = "Credenziali aggiornate"
                        val utente = parseJsonToModel(
                            SharedPreferencesManager.getString(
                                "utente",
                                ""
                            )
                        )
                        SharedPreferencesManager.saveString(
                            "utente",
                            parseModelToJson(Utente(utente.id, username, utente.password, email))
                        )
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject>?, t:
                    Throwable?
                ) {
                    _stringaCredenziali.value = "Errore di connessione"
                }
            }
        )
    }

    private val _disponibilitàModifica = MutableLiveData<Boolean?>()
    val disponibilitàModifica: LiveData<Boolean?>
        get() = _disponibilitàModifica

    init {
        _disponibilitàModifica.value = null
    }

    fun checkModifica(email: String, username: String) {
        Client.retrofit.checkDisponibilita(username, email).enqueue(
            object : Callback<JsonArray> {
                override fun onResponse(
                    call: Call<JsonArray>, response:
                    Response<JsonArray>
                ) {
                    if (response.isSuccessful) {
                        _disponibilitàModifica.value = response.body().isEmpty
                    }
                }

                override fun onFailure(
                    call: Call<JsonArray>?, t:
                    Throwable?
                ) {

                }

            }
        )
    }

    private val _modificaPassword = MutableLiveData<Boolean?>()
    val modificaPassword: LiveData<Boolean?>
        get() = _modificaPassword

    init {
        _modificaPassword.value = null
    }

    fun changeProfilePassword(password: String, mail: String) {
        val gson =
            JsonParser.parseString(parseModelToJson(ModifyPasswordProfile(password, mail)))
        Client.retrofit.cambiapassword(gson.asJsonObject).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response:
                    Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        _modificaPassword.value = true

                    }
                }

                override fun onFailure(
                    call: Call<JsonObject>?, t:
                    Throwable?
                ) {
                    _modificaPassword.value = false
                }
            }
        )

    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private val _InvitiOttenuti = MutableLiveData<Boolean?>()
    val InvitiOttenuti: LiveData<Boolean?>
        get() = _InvitiOttenuti

    init {
        _InvitiOttenuti.value = null
    }

    private val _listaInviti = MutableLiveData<ArrayList<Lega>>()
    val listaInviti: LiveData<ArrayList<Lega>>
        get() = _listaInviti

    init {
        _listaInviti.value = arrayListOf()
    }


    public fun getInvitiUtente() {
        Client.retrofit.getInvitiUtente(_user.value!!.id).enqueue(object : Callback<JsonArray> {
            override fun onResponse(
                call: Call<JsonArray>, response:
                Response<JsonArray>
            ) {
                if (response.isSuccessful) {
                    _listaInviti.value = (parseJsonToLeghe(response.body().toString()))
                    _InvitiOttenuti.value = true
                }
            }

            override fun onFailure(
                call: Call<JsonArray>?, t:
                Throwable?
            ) {
                _InvitiOttenuti.value = false
            }

        })

    }

    public fun resetInvitiOttenuti() {
        _InvitiOttenuti.value = null
    }
    private val _accettando=MutableLiveData<Boolean?>()
    val accettando: LiveData<Boolean?>
        get() = _accettando
    init {
        _accettando.value=null
    }
    fun resetaccettando(){
        _accettando.value=null
    }
    fun accettainvito(idlega:Int,idutente:Int,npart:Int){
        _accettando.value=true
        val body = Gson().fromJson(
            parseModelToJson(UtenteLegaPart(idutente,idlega,npart)),
            JsonObject::class.java
        )
        Client.retrofit.accettautente(body).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response:
                    Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        _accettando.value = false
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject>?, t:
                    Throwable?
                ) {
                    _accettando.value = false
                }
            }
        )

    }
    fun rifiutainvito(idlega:Int,idutente:Int){
        val body = Gson().fromJson(
            parseModelToJson(UtenteLega(idutente,idlega)),
            JsonObject::class.java
        )
        Client.retrofit.rifiutautente(body).enqueue(
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

}

class Emailcode(val email: String, val codice: Int)
class ModifyCredenzialiProfile(val email: String, val username: String, val id: Int)

class ModifyPasswordProfile(val password: String, val email: String)

fun parseJsonToModel(jsonString: String): Utente {
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
