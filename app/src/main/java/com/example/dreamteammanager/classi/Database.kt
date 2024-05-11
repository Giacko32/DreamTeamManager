package com.example.dreamteammanager.classi

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dreamteammanager.viewmodel.UtenteDao

@Database(entities = [Utente::class,Lega::class,Competizione::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun utenteDao(): UtenteDao


}