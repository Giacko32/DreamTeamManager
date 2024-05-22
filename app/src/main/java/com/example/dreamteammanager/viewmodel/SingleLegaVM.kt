package com.example.dreamteammanager.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.classi.Lega
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.lega.InvitaGiocatoriAdapter
import com.example.dreamteammanager.lega.LegaActivity
import com.example.dreamteammanager.lega.PartecipantiAdapter
import com.example.dreamteammanager.main.LegheAdapter
import com.example.dreamteammanager.retrofit.Client
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
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
    private val _richiedenti=MutableLiveData<ArrayList<Utente>>()
    val richiedenti:LiveData<ArrayList<Utente>> = _richiedenti
    init {
        _richiedenti.value=ArrayList()
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
                        Log.d("richiedenti",response.body().toString())
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
    fun resetaccettando(){
        _accettando.value=null
    }
    fun accettautente(idutente:Int){
        _accettando.value=true
        val gson =
            JsonParser.parseString(parseModelToJson(UtenteLegaPart(idutente, _lega.value!!.id,_lega.value!!.numeropartecipanti)))
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
    fun eliminarichiedente(idutente: Int){
        richiedenti.value?.forEach{ utente->
            if(utente.id==idutente){
                _richiedenti.value?.removeAt(richiedenti.value!!.indexOf(utente))
            }
        }
    }
    private val _caricadati = MutableLiveData<Boolean?>()
    val caricadati: LiveData<Boolean?>
        get() = _caricadati

    init {
        _caricadati.value = null
    }
    fun resetcaricadati(){
        _caricadati.value=null
    }
    fun updatecaricadati(){
        _caricadati.value=true
    }
    fun rifiutautente(idutente: Int){
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
    fun resetutentiinvito(){
        _utentiinivito.value=null
    }
    private val _invitanti=MutableLiveData<ArrayList<Utente>>()
    val invitati:LiveData<ArrayList<Utente>> = _invitanti
    init {
        _invitanti.value=ArrayList()
    }
    fun getutentiinivito(){
        _utentiinivito.value=false
        _invitanti.value?.clear()
        Client.retrofit.getnonpartecipanti(lega.value!!.id).enqueue(
            object : Callback<JsonArray> {
                override fun onResponse(
                    call: Call<JsonArray>, response:
                    Response<JsonArray>
                ) {
                    if (response.isSuccessful) {
                        _invitanti.value = parseJsonToArrayUtenti(response.body().toString())
                        _utentiinivito.value=true
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
    private val _invitatifiltrato=MutableLiveData<ArrayList<Utente>>()
    val invitatifiltrato:LiveData<ArrayList<Utente>>
        get()=_invitatifiltrato
    init {
        _invitatifiltrato.value= arrayListOf()
    }
    fun setinvitatiFiltrato(filtro:String){
        _invitatifiltrato.value!!.clear()
        invitati.value!!.forEach{
            Log.d("invitati",filtro)
            if(it.username.contains(filtro)){
                _invitatifiltrato.value?.add(it)

            }

        }
        Log.d("invitati",_invitatifiltrato.value.toString())

    }





}

    fun parseJsonToArrayUtenti(jsonString: String): ArrayList<Utente> {
        val gson = Gson()
        return gson.fromJson(
            jsonString,
            object : com.google.gson.reflect.TypeToken<ArrayList<Utente>>() {}.type
        )
    }
class UtenteLegaPart(val idutente:Int,val idlega:Int,val npart:Int)