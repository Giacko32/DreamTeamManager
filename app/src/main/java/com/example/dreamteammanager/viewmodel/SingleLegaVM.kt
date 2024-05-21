package com.example.dreamteammanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dreamteammanager.classi.Lega
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.retrofit.Client
import com.google.gson.Gson
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingleLegaVM:ViewModel() {

    private val _lega= MutableLiveData<Lega?>()
    val lega: LiveData<Lega?> = _lega
    init {
        _lega.value=null
    }
    fun setlega(lega:Lega){
        _lega.value=lega
    }
    private val _partecipanti=MutableLiveData<ArrayList<Utente>>()
    val partecipanti:LiveData<ArrayList<Utente>> = _partecipanti
    init {
        _partecipanti.value=ArrayList()
    }
    private val _scaricando = MutableLiveData<Boolean?>()
    val scaricando: LiveData<Boolean?>
        get() = _scaricando

    init {
        _scaricando.value = null
    }
    fun resetscaricando(){
        _scaricando.value=null
    }
    fun getPartecipanti(){
        _scaricando.value=true
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
}

    fun parseJsonToArrayUtenti(jsonString: String): ArrayList<Utente> {
        val gson = Gson()
        return gson.fromJson(
            jsonString,
            object : com.google.gson.reflect.TypeToken<Utente>() {}.type
        )
    }