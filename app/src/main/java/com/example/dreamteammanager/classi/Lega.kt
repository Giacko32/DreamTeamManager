package com.example.dreamteammanager.classi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class Lega(
    val id: Int,
    val name: String,
    val numeropartecipanti: Int,
    val id_amministratore:Int
): Parcelable