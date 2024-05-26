package com.example.dreamteammanager.classi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UtentePunteggio (
    val id: Int,
    var username: String,
    val punteggio: Int,
): Parcelable {
    override fun toString(): String {
        return this.id.toString() + " " + this.username + " " + this.punteggio
    }
}