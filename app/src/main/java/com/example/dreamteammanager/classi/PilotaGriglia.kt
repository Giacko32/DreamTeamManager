package com.example.dreamteammanager.classi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PilotaGriglia(
    var id: Int,
    var nome:String,
    var team:String,
): Parcelable