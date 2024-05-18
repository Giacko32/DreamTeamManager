package com.example.dreamteammanager.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dreamteammanager.classi.Lega
import com.example.dreamteammanager.classi.RegistraUtente
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.retrofit.Client
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class legheVM : ViewModel() {
    private val _listaLeghe = MutableLiveData<ArrayList<Lega>>()
    val listaLeghe: LiveData<ArrayList<Lega>>
        get() = _listaLeghe

    init {
        _listaLeghe.value = ArrayList()
    }


    fun addLega(lega: Lega) {
        _listaLeghe.value?.add(lega)
    }

    fun clear() {
        _listaLeghe.value?.clear()
    }

    private val _mieleghe = MutableLiveData<Boolean>()
    val mieleghe: LiveData<Boolean>
        get() = _mieleghe

    init {
        _mieleghe.value = true
    }

    fun setMieleghe(mieleghe: Boolean) {
        _mieleghe.value = mieleghe
    }

    private val _scaricando = MutableLiveData<Boolean?>()
    val scaricando: LiveData<Boolean?>
        get() = _scaricando

    init {
        _scaricando.value = null
    }

    fun scaricaleghe(id: Int) {
        _scaricando.value = true
        if (_mieleghe.value == true) {
            Client.retrofit.scaricamieleghe(id).enqueue(
                object : Callback<JsonArray> {
                    override fun onResponse(
                        call: Call<JsonArray>, response:
                        Response<JsonArray>
                    ) {
                        if (response.isSuccessful) {
                            _listaLeghe.value = parseJsonToLeghe(response.body().toString())
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
        } else {
            Client.retrofit.scaricatutteleghe(id).enqueue(
                object : Callback<JsonArray> {
                    override fun onResponse(
                        call: Call<JsonArray>, response:
                        Response<JsonArray>
                    ) {
                        if (response.isSuccessful) {
                            _listaLeghe.value = parseJsonToLeghe(response.body().toString())
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
    }

    fun creanuovalega(lega: Lega) {
        _scaricando.value = true
        val gson = JsonParser.parseString(parseModelToJson(lega))
        Client.retrofit.insertlega(gson.asJsonObject).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response:
                    Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        aggiungialega(
                            parseJsonToModel(
                                SharedPreferencesManager.getString(
                                    "utente",
                                    ""
                                )
                            ), parseJsonToLega(response.body().toString())
                        )
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

    private val _inviando = MutableLiveData<Boolean?>()
    val inviando: LiveData<Boolean?>
        get() = _inviando

    init {
        _inviando.value = null
    }

    fun resetinviando() {
        _inviando.value = null
    }

    fun aggiungialega(utente: Utente, lega: Lega) {
        _scaricando.value = true
        val gson = JsonParser.parseString(parseModelToJson(UtenteLega(utente.id, lega.id)))
        Client.retrofit.insertutente(gson.asJsonObject).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response:
                    Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        _scaricando.value = false

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

    fun chiedidipartecipare(utente: Utente, lega: Lega) {
        _inviando.value = true
        val gson = JsonParser.parseString(parseModelToJson(UtenteLega(utente.id, lega.id)))
        Client.retrofit.richiestadipart(gson.asJsonObject).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response:
                    Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        _inviando.value = false
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

    private val _checkrichiesta = MutableLiveData<Boolean?>()
    val checkrichiest: LiveData<Boolean?>
        get() = _checkrichiesta

    init {
        _checkrichiesta.value = null
    }

    fun resetcheckrichiesta() {
        _checkrichiesta.value = null
    }

    fun checkrichiesta(utente: Utente, lega: Lega) {
        _scaricando.value = true
        Client.retrofit.checkrequest(lega.id, utente.id).enqueue(
            object : Callback<JsonArray> {
                override fun onResponse(
                    call: Call<JsonArray>, response:
                    Response<JsonArray>
                ) {
                    if (response.isSuccessful) {
                        Log.d("TAG", response.body().toString())
                        if (response.body().toString() == "[]") {
                            _checkrichiesta.value = true
                        } else {
                            _checkrichiesta.value = false
                        }
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

    private val _selectedlega = MutableLiveData<Lega?>()
    val selectedlega: LiveData<Lega?>
        get() = _selectedlega

    init {
        _selectedlega.value = null
    }

    fun setSelectedLeague(lega: Lega) {
        _selectedlega.value = lega
    }

}

fun parseJsonToLeghe(jsonString: String): ArrayList<Lega> {
    val gson = Gson()
    return gson.fromJson(
        jsonString,
        object : com.google.gson.reflect.TypeToken<ArrayList<Lega>>() {}.type
    )
}

fun parseJsonToLega(jsonString: String): Lega {
    val gson = Gson()
    return gson.fromJson(
        jsonString,
        object : com.google.gson.reflect.TypeToken<Lega>() {}.type
    )
}

class UtenteLega(val idutente: Int, val idlega: Int)