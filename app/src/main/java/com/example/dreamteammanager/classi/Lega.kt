package com.example.dreamteammanager.classi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Lega(
    @PrimaryKey val id: Int,
    val name: String,
    val numeropartecipanti: Int,
)