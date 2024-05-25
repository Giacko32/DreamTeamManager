package com.example.dreamteammanager.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.dreamteammanager.R


class NotificaInattivita(val context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters)  {

    private val CHANNEL_ID = "inactivity_notifications"
    private val NOTIFICATION_ID = 1
    override fun doWork(): Result {
        createNotificationChannel()
        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.icona)
            .setContentTitle("Torna a trovarci!")
            .setContentText("È passato un po' di tempo da quando hai aperto l'app.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        return Result.success()
    }

    private fun createNotificationChannel() {
        val name = "Notifiche di inattività"
        val descriptionText = "Canale per notifiche di inattività"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}