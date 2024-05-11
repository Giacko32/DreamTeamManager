package com.example.dreamteammanager.classi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "utenti")
data class Utente(
    @PrimaryKey val id: Int,
    val username:String,
    var password:String,
    val email:String)
