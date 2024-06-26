package com.example.dreamteammanager.classi

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Utente(
    val id: Int,
    var username: String,
    var password: String,
    val email: String,
) : Parcelable {
    override fun toString(): String {
        return this.id.toString() + " " + this.username + " " + this.password + " " + this.email
    }
}
