package com.example.dreamteammanager.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dreamteammanager.classi.Competizione
import com.example.dreamteammanager.classi.Giocatore
import com.example.dreamteammanager.classi.GiocatoreFormazione
import com.example.dreamteammanager.classi.GiocatoreStatistiche
import com.example.dreamteammanager.classi.Pilota
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.classi.UtentePunteggio
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

    private val _scaricandoclassifica = MutableLiveData<Boolean?>()
    val scaricandoclassifica: LiveData<Boolean?>
        get() = _scaricandoclassifica

    init {
        _scaricandoclassifica.value = null
    }

    fun resetscaricandoclassifica() {
        _scaricandoclassifica.value = null
    }

    private val _classifica = MutableLiveData<ArrayList<UtentePunteggio>>()
    val classifica: LiveData<ArrayList<UtentePunteggio>> = _classifica

    init {
        _classifica.value = arrayListOf()
    }

    fun getClassifica() {
        _scaricandoclassifica.value = true
        _classifica.value?.clear()
        Client.retrofit.getClassifica(competizione.value!!.id).enqueue(
            object : Callback<JsonArray> {
                override fun onResponse(
                    call: Call<JsonArray>, response:
                    Response<JsonArray>
                ) {
                    if (response.isSuccessful) {
                        _classifica.value = parseJsonToArrayClassifica(response.body().toString())
                        _scaricandoclassifica.value = false
                    }
                }

                override fun onFailure(
                    call: Call<JsonArray>?, t:
                    Throwable?
                ) {
                    _scaricandoclassifica.value = false
                }
            }
        )
    }

    private val _scaricandostatistiche = MutableLiveData<Boolean?>()
    val scaricandostatistiche: LiveData<Boolean?>
        get() = _scaricandostatistiche

    init {
        _scaricandostatistiche.value = null
    }

    fun resetscaricandostatistiche() {
        _scaricandostatistiche.value = null
    }

    private val _statistiche = MutableLiveData<ArrayList<GiocatoreStatistiche>>()
    val statistiche: LiveData<ArrayList<GiocatoreStatistiche>> = _statistiche

    init {
        _statistiche.value = arrayListOf()
    }


    fun getStatistica() {
        _scaricandostatistiche.value = true
        _statistiche.value?.clear()
        Client.retrofit.getStatistica(competizione.value!!.sport).enqueue(
            object : Callback<JsonArray> {
                override fun onResponse(
                    call: Call<JsonArray>, response:
                    Response<JsonArray>
                ) {
                    if (response.isSuccessful) {
                        _statistiche.value = parseJsonToArrayStatistica(response.body().toString())
                        _scaricandostatistiche.value = false
                    }
                }

                override fun onFailure(
                    call: Call<JsonArray>?, t:
                    Throwable?
                ) {
                    _scaricandostatistiche.value = false
                }
            }
        )
    }

    private val _giocatoridisp = MutableLiveData<ArrayList<GiocatoreFormazione>>()
    val giocatoridisp: LiveData<ArrayList<GiocatoreFormazione>> = _giocatoridisp

    init {
        _giocatoridisp.value = arrayListOf()
    }

    private val _scaricandogioc = MutableLiveData<Boolean?>()
    val scaricandogioc: LiveData<Boolean?>
        get() = _scaricandogioc

    init {
        _scaricandogioc.value = null
    }

    fun resetscaricandogioc() {
        _scaricandogioc.value = null
    }

    fun getgiocatoridisp() {
        _scaricandogioc.value = true
        _giocatoridisp.value?.clear()
        Client.retrofit.getGiocatoriDisp(competizione.value!!.sport, competizione.value!!.id)
            .enqueue(
                object : Callback<JsonArray> {
                    override fun onResponse(
                        call: Call<JsonArray>, response:
                        Response<JsonArray>
                    ) {
                        if (response.isSuccessful) {
                            _giocatoridisp.value =
                                parseJsonToArrayGiocatori(response.body().toString())
                            _scaricandogioc.value = false
                        }
                    }

                    override fun onFailure(
                        call: Call<JsonArray>?, t:
                        Throwable?
                    ) {
                        _scaricandogioc.value = false
                    }
                }
            )


    }

    private fun rimuovigioc(giocatore: GiocatoreFormazione) {
        _giocatoridisp.value?.remove(giocatore)
    }

    private val _aggiungendogioc = MutableLiveData<Boolean?>()
    val aggiungendogioc: LiveData<Boolean?>
        get() = _aggiungendogioc

    init {
        _aggiungendogioc.value = null
    }

    fun resetaggiungendogioc() {
        _aggiungendogioc.value = null
    }

    fun inseriscigiocatore(giocatore: GiocatoreFormazione, utente: Utente) {
        _aggiungendogioc.value = true
        rimuovigioc(giocatore)
        val body = Gson().fromJson(
            parseModelToJson(
                InsertGiocatore(
                    competizione.value!!,
                    giocatore,
                    utente
                )
            ), JsonObject::class.java
        )
        Client.retrofit.insertgioc(body).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response:
                    Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        _aggiungendogioc.value = false
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject>?, t:
                    Throwable?
                ) {
                    _aggiungendogioc.value = false
                }
            }
        )
    }

    private val _aggiungendoformazione = MutableLiveData<Boolean?>()
    val aggiungendoformazione: LiveData<Boolean?>
        get() = _aggiungendoformazione

    init {
        _aggiungendoformazione.value = null
    }

    fun resetaggiungendoformazione() {
        _aggiungendoformazione.value = null
    }

    private var _formazione = MutableLiveData<IntArray>()
    val formazione: LiveData<IntArray>
        get() = _formazione

    init {
        _formazione.value = IntArray(11)
    }

    private var _griglia = MutableLiveData<IntArray>()
    val griglia: LiveData<IntArray>
        get() = _griglia

    init {
        _griglia.value = IntArray(2)
    }


    fun inseresciFormazione(utente: Utente, giornata: Int) {
        _aggiungendoformazione.value = true
        val body: JsonObject
        if (competizione.value!!.sport == "Serie A") {
            body = Gson().fromJson(
                parseModelToJson(
                    Formazione(
                        utente.id,
                        giornata,
                        competizione.value!!,
                        formazione.value!!,
                    )
                ), JsonObject::class.java
            )
        } else {
            body = Gson().fromJson(
                parseModelToJson(
                    Formazione(
                        utente.id,
                        giornata,
                        competizione.value!!,
                        griglia.value!!,
                    )
                ), JsonObject::class.java
            )
        }
        Client.retrofit.insertFormazione(body).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>, response:
                    Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        _aggiungendoformazione.value = false
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject>?, t:
                    Throwable?
                ) {
                    _aggiungendoformazione.value = false
                }
            }
        )
    }


    private val _rosaGiocatori = MutableLiveData<ArrayList<GiocatoreFormazione>?>()
    val rosaGiocatori: LiveData<ArrayList<GiocatoreFormazione>?>
        get() = _rosaGiocatori

    init {
        _rosaGiocatori.value = null
    }

    private val _rosaPiloti = MutableLiveData<ArrayList<Pilota>?>()
    val rosaPiloti: LiveData<ArrayList<Pilota>?>
        get() = _rosaPiloti

    init {
        _rosaPiloti.value = null
    }

    private val _rosaottenuta = MutableLiveData<Boolean?>()
    val rosaottenuta: LiveData<Boolean?>
        get() = _rosaottenuta

    init {
        _rosaottenuta.value = null
    }

    private val _giornate = MutableLiveData<ArrayList<Giornata>>()
    val giornate: LiveData<ArrayList<Giornata>> = _giornate

    init {
        _giornate.value = arrayListOf()
    }

    fun getRosaGiocatore(id_Utente: Int) {
        _rosaottenuta.value = false
        _giornate.value?.clear()
        Client.retrofit.getRosa(competizione.value!!.sport, competizione.value!!.id, id_Utente)
            .enqueue(object : Callback<JsonArray> {
                override fun onResponse(
                    call: Call<JsonArray>, response:
                    Response<JsonArray>
                ) {
                    if (response.isSuccessful) {
                        if (competizione.value!!.sport == "Serie A") {
                            _rosaGiocatori.value =
                                parseJsonToArrayGiocatori(response.body().toString())
                        } else {
                            _rosaPiloti.value = parseJsonToArrayPiloti(response.body().toString())
                        }
                        Client.retrofit.getgiornate(competizione.value!!.id, id_Utente).enqueue(
                            object : Callback<JsonArray> {
                                override fun onResponse(
                                    call: Call<JsonArray>, response:
                                    Response<JsonArray>
                                ) {
                                    if (response.isSuccessful) {
                                        _giornate.value =
                                            parseJsonToArrayInt(response.body().toString())
                                        Log.d("giornate", _giornate.value.toString())
                                        _rosaottenuta.value = true
                                    }
                                }

                                override fun onFailure(
                                    call: Call<JsonArray>?, t:
                                    Throwable?
                                ) {
                                    _rosaottenuta.value = false
                                }
                            }
                        )
                    }
                }

                override fun onFailure(
                    call: Call<JsonArray>?, t:
                    Throwable?
                ) {

                }
            })
    }

    fun filtro(ruolo: String): List<GiocatoreFormazione> {
        val risultato = mutableListOf<GiocatoreFormazione>()

        for (giocatore in _rosaGiocatori.value!!) {
            if (giocatore.ruolo.equals(ruolo)) {
                if (!(checkInserito(giocatore.id))) {
                    risultato.add(giocatore)
                }
            }
        }

        return risultato
    }

    private fun checkInserito(idg: Int): Boolean {
        for (id in formazione.value!!) {
            if (id == idg) {
                return true
            }
        }
        return false
    }

    fun checkForm(): Boolean {
        for (id in formazione.value!!) {
            if (id == 0) {
                return false
            }
        }
        return true
    }

    fun filtrapiloti(): List<Pilota> {
        val risultato = mutableListOf<Pilota>()
        for (pilota in rosaPiloti.value!!) {
            if (!(checkInseritopil(pilota.idpilota))) {
                risultato.add(pilota)
            }
        }
        return risultato
    }

    private fun checkInseritopil(idp: Int): Boolean {
        for (id in griglia.value!!) {
            if (id == idp) {
                return true
            }
        }
        return false
    }

    fun clear() {
        _formazione.value = IntArray(11)
        _griglia.value = IntArray(2)
    }

}

fun parseJsonToArrayInt(jsonString: String): ArrayList<Giornata> {
    val gson = Gson()
    return gson.fromJson(
        jsonString,
        object : com.google.gson.reflect.TypeToken<ArrayList<Giornata>>() {}.type
    )
}

fun parseJsonToArrayClassifica(jsonString: String): ArrayList<UtentePunteggio> {
    val gson = Gson()
    return gson.fromJson(
        jsonString,
        object : com.google.gson.reflect.TypeToken<ArrayList<UtentePunteggio>>() {}.type
    )
}

fun parseJsonToArrayStatistica(jsonString: String): ArrayList<GiocatoreStatistiche> {
    val gson = Gson()
    return gson.fromJson(
        jsonString,
        object : com.google.gson.reflect.TypeToken<ArrayList<GiocatoreStatistiche>>() {}.type
    )
}

fun parseJsonToArrayGiocatori(jsonString: String): ArrayList<GiocatoreFormazione> {
    val gson = Gson()
    return gson.fromJson(
        jsonString,
        object : com.google.gson.reflect.TypeToken<ArrayList<GiocatoreFormazione>>() {}.type
    )
}

fun parseJsonToArrayPiloti(jsonString: String): ArrayList<Pilota> {
    val gson = Gson()
    return gson.fromJson(
        jsonString,
        object : com.google.gson.reflect.TypeToken<ArrayList<Pilota>>() {}.type
    )
}


class CompGiorn(val competizione: Competizione, val giornata: Int)
class Giornata(val giornata: Int)

class InsertGiocatore(
    val comp: Competizione,
    val giocatore: GiocatoreFormazione,
    val utente: Utente
)

class Formazione(
    val idUtente: Int,
    val giornata: Int,
    val comp: Competizione,
    val giocatori: IntArray,
)