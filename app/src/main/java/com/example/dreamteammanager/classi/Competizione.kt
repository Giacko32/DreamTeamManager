package com.example.dreamteammanager.classi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Competizione(
    var id: Int,
    var nome:String,
    var sport:String,
    val idlega:Int
):Parcelable