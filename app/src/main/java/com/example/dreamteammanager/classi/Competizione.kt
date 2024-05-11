package com.example.dreamteammanager.classi

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
@Entity(
    foreignKeys = [ForeignKey(
        entity = Lega::class,
        parentColumns = ["id"],
        childColumns = ["lega_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Competizione(
    @PrimaryKey val id: Int,
    val nome:String,
    val tipo:Int,
    val lega_id:Int
)