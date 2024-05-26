package com.example.dreamteammanager.classi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class GiocatoreStatistiche(
    var id: Int,
    var nome:String,
    var ruolo:String,
    val mediaVoti:Double,
): Parcelable