package com.example.dreamteammanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dreamteammanager.classi.Competizione
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.retrofit.Client
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CompetizioniVM : ViewModel() {

    private var _competizione = MutableLiveData<Competizione?>()
    val competizione: LiveData<Competizione?>
        get() = _competizione

    init {
        _competizione.value = null
    }

    private val _idamm = MutableLiveData<Int?>()
    val idamm: LiveData<Int?>
        get() = _idamm

    init {
        _idamm.value = null
    }

    fun setidamm(id: Int) {
        _idamm.value = id
    }

    private val _sport = MutableLiveData<String?>()
    val sport: LiveData<String?>
        get() = _sport

    init {
        _sport.value = null
    }

    public fun setCompetizione(c: Competizione) {
        _competizione.value = c
    }

    public fun setSport(s: String) {
        _sport.value = s
        _competizione.value?.sport = s
    }

    public fun setnome(s: String) {
        _competizione.value?.nome = s
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

    fun getpartecipanti() {
        _scaricando.value = true
        _partecipanti.value?.clear()
        Client.retrofit.getpart(competizione.value!!.id).enqueue(
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

    private val _prendendo = MutableLiveData<Boolean?>()
    val prendendo: LiveData<Boolean?>
        get() = _prendendo

    init {
        _prendendo.value = null
    }

    fun resetprendendo() {
        _prendendo.value = null
    }

    private val _giornatedacalcolare = MutableLiveData<ArrayList<Giornata>>()
    val giornatedacalcolare: LiveData<ArrayList<Giornata>> = _giornatedacalcolare

    init {
        _giornatedacalcolare.value = arrayListOf()
    }

    fun getgiornatedacalcolare() {
        _prendendo.value = true
        _giornatedacalcolare.value?.clear()
        Client.retrofit.getgiornatedacalcolare(competizione.value!!.id).enqueue(
            object : Callback<JsonArray> {
                override fun onResponse(
                    call: Call<JsonArray>, response:
                    Response<JsonArray>
                ) {
                    if (response.isSuccessful) {
                        _giornatedacalcolare.value = parseJsonToArrayInt(response.body().toString())
                        _prendendo.value = false
                    }
                }

                override fun onFailure(
                    call: Call<JsonArray>?, t:
                    Throwable?
                ) {
                    _prendendo.value = false
                }
            }
        )
    }

    private val _calcolando = MutableLiveData<Boolean?>()
    val calcolando: LiveData<Boolean?>
        get() = _calcolando

    init {
        _calcolando.value = null
    }

    fun resetcalcolando() {
        _calcolando.value = null
    }

    fun calcolagiornata(giornata: Int) {
        _calcolando.value = true
        val body = Gson().fromJson(
            parseModelToJson(CompGiorn(competizione.value!!, giornata)),
            JsonObject::class.java
        )
        Client.retrofit.calcolagiornata(body).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response:
                    Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        _calcolando.value = false
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject>?, t:
                    Throwable?
                ) {
                    _calcolando.value = false
                }
            }
        )
    }
}

    fun parseJsonToArrayInt(jsonString: String): ArrayList<Giornata> {
        val gson = Gson()
        return gson.fromJson(
            jsonString,
            object : com.google.gson.reflect.TypeToken<ArrayList<Giornata>>() {}.type
        )
    }

    class CompGiorn(val competizione: Competizione, val giornata: Int)
    class Giornata(val giornata: Int)