package com.example.dreamteammanager.classi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class GiocatoreFormazione(
    var id: Int,
    var nome:String,
    var ruolo:String,
): Parcelable