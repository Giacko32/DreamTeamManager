package com.example.dreamteammanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dreamteammanager.classi.Competizione


class CompetizioniVM: ViewModel(){

    private val _competizione = MutableLiveData<Competizione?>()
    val competizione: LiveData<Competizione?>
        get() = _competizione
    init {
        _competizione.value = null
    }

    private val _sport = MutableLiveData<String?>()
    val sport: LiveData<String?>
        get() = _sport
    init {
        _sport.value = null
    }

    public fun setCompetizione( c: Competizione)
    {
        _competizione.value = c
    }

    public fun setSport( s: String)
    {
        _sport.value = s
    }
}