package com.example.dreamteammanager.classi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class Lega(
    val id: Int,
    var name: String,
    var image: Int,
    var numeropartecipanti: Int,
    val id_amministratore:Int
): Parcelable