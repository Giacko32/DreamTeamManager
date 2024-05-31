package com.example.dreamteammanager.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dreamteammanager.classi.Competizione
import com.example.dreamteammanager.classi.CompetizioneLegaPart
import com.example.dreamteammanager.classi.Lega
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.classi.UtenteLega
import com.example.dreamteammanager.classi.UtenteLegaPart
import com.example.dreamteammanager.classi.Utils.Companion.parseJsonToArrayComp
import com.example.dreamteammanager.classi.Utils.Companion.parseJsonToArrayUtenti
import com.example.dreamteammanager.classi.Utils.Companion.parseModelToJson
import com.example.dreamteammanager.retrofit.Client
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingleLegaVM : ViewModel() {

    private val _lega = MutableLiveData<Lega?>()
    val lega: LiveData<Lega?> = _lega

    init {
        _lega.value = null
    }

    fun setlega(lega: Lega) {
        _lega.value = lega
    }

    private val _partecipanti = MutableLiveData<ArrayList<Utente>>()
    val partecipanti: LiveData<ArrayList<Utente>> = _partecipanti

    init {
        _partecipanti.value = ArrayList()
    }

    private val _scaricando = MutableLiveData<Boolean?>()
    val scaricando: LiveData<Boolean?>
        get() = _scaricando

    init {
        _scaricando.value = null
    }

    fun resetscaricando() {
        _scaricando.value = null
    }

    fun getPartecipanti() {
        _scaricando.value = true
        _partecipanti.value?.clear()
        Client.retrofit.getPartecipanti(_lega.value!!.id).enqueue(
            object : Callback<JsonArray> {
                override fun onResponse(
                    call: Call<JsonArray>, response:
                    Response<JsonArray>
                ) {
                    if (response.isSuccessful) {
                        _partecipanti.value = parseJsonToArrayUtenti(response.body().toString())
                        _scaricando.value = false
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

    private val _richiedenti = MutableLiveData<ArrayList<Utente>>()
    val richiedenti: LiveData<ArrayList<Utente>> = _richiedenti

    init {
        _richiedenti.value = ArrayList()
    }


    fun getrichiedenti() {
        _scaricando.value = true
        _richiedenti.value?.clear()
        Client.retrofit.getrichiedenti(_lega.value!!.id).enqueue(
            object : Callback<JsonArray> {
                override fun onResponse(
                    call: Call<JsonArray>, response:
                    Response<JsonArray>
                ) {
                    if (response.isSuccessful) {
                        Log.d("richiedenti", response.body().toString())
                        _richiedenti.value = parseJsonToArrayUtenti(response.body().toString())
                        _scaricando.value = false
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

    private val _accettando = MutableLiveData<Boolean?>()
    val accettando: LiveData<Boolean?>
        get() = _accettando

    init {
        _accettando.value = null
    }

    fun resetaccettando() {
        _accettando.value = null
    }

    fun accettautente(idutente: Int) {
        _accettando.value = true
        val gson =
            JsonParser.parseString(
                parseModelToJson(
                    UtenteLegaPart(
                        idutente,
                        _lega.value!!.id,
                        _lega.value!!.numeropartecipanti
                    )
                )
            )
        Client.retrofit.accettautente(gson.asJsonObject).enqueue(object : Callback<JsonObject> {
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

            }
        })
    }

    fun eliminarichiedente(idutente: Int) {
        var index = -1
        richiedenti.value?.forEach { utente ->
            if (utente.id == idutente) {
                index = richiedenti.value!!.indexOf(utente)
            }
        }
        if (index != -1) {
            _richiedenti.value?.removeAt(index)
        }
    }

    private val _caricadati = MutableLiveData<Boolean?>()
    val caricadati: LiveData<Boolean?>
        get() = _caricadati

    init {
        _caricadati.value = null
    }

    fun resetcaricadati() {
        _caricadati.value = null
    }

    fun updatecaricadati() {
        _caricadati.value = true
    }

    fun rifiutautente(idutente: Int) {
        val gson =
            JsonParser.parseString(parseModelToJson(UtenteLega(idutente, _lega.value!!.id)))
        Client.retrofit.rifiutautente(gson.asJsonObject).enqueue(
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
            })

    }

    private val _utentiinivito = MutableLiveData<Boolean?>()
    val utentiinvito: LiveData<Boolean?>
        get() = _utentiinivito

    init {
        _utentiinivito.value = null
    }

    fun resetutentiinvito() {
        _utentiinivito.value = null
    }

    private val _invitanti = MutableLiveData<ArrayList<Utente>>()
    val invitati: LiveData<ArrayList<Utente>> = _invitanti

    init {
        _invitanti.value = ArrayList()
    }

    fun getutentiinivito() {
        _utentiinivito.value = false
        _invitanti.value?.clear()
        Client.retrofit.getnonpartecipanti(lega.value!!.id).enqueue(
            object : Callback<JsonArray> {
                override fun onResponse(
                    call: Call<JsonArray>, response:
                    Response<JsonArray>
                ) {
                    if (response.isSuccessful) {
                        _invitanti.value = parseJsonToArrayUtenti(response.body().toString())
                        _utentiinivito.value = true
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

    private val _invitatifiltrato = MutableLiveData<ArrayList<Utente>>()
    val invitatifiltrato: LiveData<ArrayList<Utente>>
        get() = _invitatifiltrato

    init {
        _invitatifiltrato.value = arrayListOf()
    }

    private val _filtrati = MutableLiveData<Boolean>()
    val filtrati: LiveData<Boolean>
        get() = _filtrati

    init {
        _filtrati.value = false
    }

    fun updatefiltrati() {
        _filtrati.value = true
    }

    fun resetfiltrati() {
        _filtrati.value = false
    }


    fun setinvitatiFiltrato(filtro: String) {
        _invitatifiltrato.value!!.clear()
        invitati.value!!.forEach {
            Log.d("invitati", filtro)
            if (it.username.contains(filtro)) {
                _invitatifiltrato.value?.add(it)

            }

        }
    }

    fun eliminainvitato(idutente: Int) {
        var index: Int = 0
        invitati.value?.forEach { utente ->
            if (utente.id == idutente) {
                index = invitati.value!!.indexOf(utente)
            }
        }
        invitati.value?.removeAt(index)
    }

    private val _invitando = MutableLiveData<Boolean?>()
    val invitando: LiveData<Boolean?>
        get() = _invitando

    init {
        _invitando.value = null
    }

    fun resetinvitando() {
        _invitando.value = null
    }

    fun invitautente(idutente: Int) {
        _invitando.value = true
        val body = Gson().fromJson(
            parseModelToJson(UtenteLega(idutente, _lega.value!!.id)),
            JsonObject::class.java
        )
        Client.retrofit.invitautente(body).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response:
                    Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        _invitando.value = false
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject>?, t:
                    Throwable?
                ) {
                    _invitando.value = false
                }
            }
        )


    }

    private val _listapartcompiti = MutableLiveData<ArrayList<Utente>>()
    val listapartcompiti: LiveData<ArrayList<Utente>> = _listapartcompiti

    init {
        _listapartcompiti.value = ArrayList()
    }

    private val _creando = MutableLiveData<Boolean?>()
    val creando: LiveData<Boolean?>
        get() = _creando

    init {
        _creando.value = null
    }

    fun resetcreando() {
        _creando.value = null
    }

    fun creacompetizione(competizione: Competizione) {
        _creando.value = true
        val body = Gson().fromJson(
            parseModelToJson(
                CompetizioneLegaPart(
                    competizione,
                    _lega.value!!.id,
                    listapartcompiti.value!!
                )
            ),
            JsonObject::class.java
        )
        Client.retrofit.creacomp(body).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response:
                    Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        _creando.value = false
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject>?, t:
                    Throwable?
                ) {
                    _creando.value = false
                }
            }
        )

    }

    private val _listacompetizioni = MutableLiveData<ArrayList<Competizione>>()
    val listacompetizioni: LiveData<ArrayList<Competizione>> = _listacompetizioni

    init {
        _listacompetizioni.value = ArrayList()
    }

    private val _scarcomp = MutableLiveData<Boolean?>()
    val scarcomp: LiveData<Boolean?>
        get() = _scarcomp

    init {
        _scarcomp.value = null
    }

    fun resetcomp() {
        _scarcomp.value = null
    }

    fun getcompetizioni() {
        _scarcomp.value = true
        Client.retrofit.getcomp(lega.value!!.id).enqueue(
            object : Callback<JsonArray> {
                override fun onResponse(
                    call: Call<JsonArray>, response:
                    Response<JsonArray>
                ) {
                    if (response.isSuccessful) {
                        _listacompetizioni.value = parseJsonToArrayComp(response.body().toString())
                        _scarcomp.value = false
                    }
                }

                override fun onFailure(
                    call: Call<JsonArray>?, t:
                    Throwable?
                ) {
                    _scarcomp.value = false

                }
            }
        )
    }


}




