package com.example.dreamteammanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dreamteammanager.classi.Lega
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.retrofit.Client
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
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
        _scaricando.value=null
    }
    fun scaricaleghe(id:Int){
        _scaricando.value=true
        if (_mieleghe.value == true) {
            Client.retrofit.scaricamieleghe(id).enqueue(
                object : Callback<JsonArray> {
                    override fun onResponse(
                        call: Call<JsonArray>, response:
                        Response<JsonArray>
                    ) {
                        if (response.isSuccessful) {
                            _listaLeghe.value=parseJsonToLeghe(response.body().toString())
                            _scaricando.value=false
                        }
                    }
                    override fun onFailure(
                        call: Call<JsonArray>?, t:
                        Throwable?
                    ) {

                    }
                }
            )
        }else{
            Client.retrofit.scaricatutteleghe(id).enqueue(
                object : Callback<JsonArray> {
                    override fun onResponse(
                        call: Call<JsonArray>, response:
                        Response<JsonArray>
                    ) {
                        if (response.isSuccessful) {
                            _listaLeghe.value=parseJsonToLeghe(response.body().toString())
                            _scaricando.value=false
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

}
fun parseJsonToLeghe(jsonString: String): ArrayList<Lega> {
    val gson = Gson()
    return gson.fromJson(
        jsonString,
        object : com.google.gson.reflect.TypeToken<ArrayList<Lega>>() {}.type
    )
}