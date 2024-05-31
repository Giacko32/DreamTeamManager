package com.example.dreamteammanager.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Utils.Companion.parseJsonToModel
import com.example.dreamteammanager.retrofit.Client
import com.example.dreamteammanager.viewmodel.SharedPreferencesManager

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificaCaricamentoGiornata(val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    private val CHANNEL_ID = "loadG_notification"
    private var NOTIFICATION_ID = 1

    class LastGiornataComp(val id_comp: Int, val giornata: Int?)

    class CaricaGiornataVerificata(val id_utente: Int, val id_comp: Int, val giornata: Int?)


    override fun doWork(): Result {
        SharedPreferencesManager.init(context)
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder =
            NotificationCompat.Builder(
                applicationContext,
                CHANNEL_ID
            )
                .setSmallIcon(R.drawable.icona)
                .setContentTitle("Una giornata è stata caricata")
                .setContentText("Entra nell'app per vedere com'è andata")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        createNotificationChannel()
        var lastGiornateComp: ArrayList<LastGiornataComp>
        var giornateverificate: ArrayList<LastGiornataComp>
        if (SharedPreferencesManager.getString("flagRicordami", "") == "true") {
            val utente = parseJsonToModel(SharedPreferencesManager.getString("utente", ""))
            Client.retrofit.getultimegiornate(utente.id).enqueue(
                object : Callback<JsonArray> {
                    override fun onResponse(
                        call: Call<JsonArray>, response:
                        Response<JsonArray>
                    ) {
                        if (response.isSuccessful) {
                            lastGiornateComp = Gson().fromJson(
                                response.body(),
                                object :
                                    com.google.gson.reflect.TypeToken<ArrayList<LastGiornataComp>>() {}.type
                            )
                            Client.retrofit.getgiornateverificate(utente.id).enqueue(
                                object : Callback<JsonArray> {
                                    override fun onResponse(
                                        call: Call<JsonArray>, response:
                                        Response<JsonArray>
                                    ) {
                                        if (response.isSuccessful) {
                                            giornateverificate = Gson().fromJson(
                                                response.body(),
                                                object :
                                                    com.google.gson.reflect.TypeToken<ArrayList<LastGiornataComp>>() {}.type
                                            )
                                            for (lastgiornatacomp in lastGiornateComp) {
                                                val giornataverificata =
                                                    giornateverificate.firstOrNull { it.id_comp == lastgiornatacomp.id_comp }!!
                                                if (giornataverificata.giornata == null) {
                                                    updateTabella(
                                                        utente.id,
                                                        lastgiornatacomp.id_comp,
                                                        lastgiornatacomp.giornata!!
                                                    )
                                                } else if (giornataverificata.giornata < lastgiornatacomp.giornata!!) {
                                                    NOTIFICATION_ID++;
                                                    notificationManager.notify(
                                                        NOTIFICATION_ID,
                                                        notificationBuilder.build()
                                                    )
                                                    updateTabella(
                                                        utente.id,
                                                        lastgiornatacomp.id_comp,
                                                        lastgiornatacomp.giornata
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<JsonArray>?, t:
                                        Throwable?
                                    ) {
                                    }
                                })
                        }
                    }

                    override fun onFailure(
                        call: Call<JsonArray>?, t:
                        Throwable?
                    ) {

                    }
                })
        }
        return Result.success()
    }

    private fun createNotificationChannel() {
        val name = "Notifiche per le giornate"
        val descriptionText = "Canale per notifiche di caricamento delle giornate"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun updateTabella(id_utente: Int, id_comp: Int, giornata: Int) {
        val toload = Gson().fromJson(
            Gson().toJson(
                CaricaGiornataVerificata(
                    id_utente,
                    id_comp,
                    giornata
                )
            ), JsonObject::class.java
        )
        Client.retrofit.aggiornaVerifiedGiornata(toload)
            .enqueue(
                object : Callback<JsonObject> {
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response:
                        Response<JsonObject>
                    ) {
                    }

                    override fun onFailure(
                        call: Call<JsonObject>?, t:
                        Throwable?
                    ) {
                    }
                }
            )
    }

}