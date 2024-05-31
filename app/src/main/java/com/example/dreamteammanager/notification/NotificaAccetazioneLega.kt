package com.example.dreamteammanager.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Utils.Companion.parseJsonToModel
import com.example.dreamteammanager.retrofit.Client
import com.example.dreamteammanager.viewmodel.SharedPreferencesManager

import com.google.gson.Gson
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificaAccetazioneLega(val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    private val CHANNEL_ID = "accettazione_lega_notifications"
    private var NOTIFICATION_ID = 10
    override fun doWork(): Result {
        SharedPreferencesManager.init(context)
        createNotificationChannel()
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.icona)
            .setContentTitle("Sei stato accettato!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (SharedPreferencesManager.getString("flagRicordami", "") == "true") {
            val utente = parseJsonToModel(SharedPreferencesManager.getString("utente", ""))
            Client.retrofit.getAccettazioniNotify(utente.id).enqueue(object : Callback<JsonArray> {
                override fun onResponse(
                    call: Call<JsonArray>, response:
                    Response<JsonArray>
                ) {
                    if (response.isSuccessful) {
                        val notifiche: ArrayList<AccettazioneLega> = Gson().fromJson(
                            response.body(),
                            object :
                                com.google.gson.reflect.TypeToken<ArrayList<AccettazioneLega>>() {}.type
                        )

                        for (notifica in notifiche) {
                            notificationBuilder.setContentText("Sei stato accettato nella lega " + notifica.nomelega);
                            NOTIFICATION_ID++;
                            notificationManager.notify(
                                NOTIFICATION_ID,
                                notificationBuilder.build()
                            )
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
        return Result.success()
    }

    private fun createNotificationChannel() {
        val name = "Notifiche di accettazione lega"
        val descriptionText = "Canale per notifiche di accettazione in una lega"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

class AccettazioneLega(val nomelega: String, val id_utente: Int, val id_lega: Int)