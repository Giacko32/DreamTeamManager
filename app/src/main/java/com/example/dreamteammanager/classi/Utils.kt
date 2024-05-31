package com.example.dreamteammanager.classi

import android.util.Patterns
import com.google.gson.Gson

class Utils {
    companion object{
        fun parseJsonToModel(jsonString: String): Utente {
            val gson = Gson()
            return gson.fromJson(
                jsonString,
                object : com.google.gson.reflect.TypeToken<Utente>() {}.type
            )
        }


        fun parseModelToJson(any: Any): String {
            val gson = Gson()
            return gson.toJson(any)
        }
         fun isValidEmail(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun parseJsonToArrayUtenti(jsonString: String): ArrayList<Utente> {
            val gson = Gson()
            return gson.fromJson(
                jsonString,
                object : com.google.gson.reflect.TypeToken<ArrayList<Utente>>() {}.type
            )
        }

        fun parseJsonToArrayComp(jsonString: String): ArrayList<Competizione> {
            val gson = Gson()
            return gson.fromJson(
                jsonString,
                object : com.google.gson.reflect.TypeToken<ArrayList<Competizione>>() {}.type
            )
        }
        fun parseJsonToLeghe(jsonString: String): ArrayList<Lega> {
            val gson = Gson()
            return gson.fromJson(
                jsonString,
                object : com.google.gson.reflect.TypeToken<ArrayList<Lega>>() {}.type
            )
        }

        fun parseJsonToLega(jsonString: String): Lega {
            val gson = Gson()
            return gson.fromJson(
                jsonString,
                object : com.google.gson.reflect.TypeToken<Lega>() {}.type
            )
        }
        fun parseJsonToArrayInt(jsonString: String): ArrayList<Giornata> {
            val gson = Gson()
            return gson.fromJson(
                jsonString,
                object : com.google.gson.reflect.TypeToken<ArrayList<Giornata>>() {}.type
            )
        }

        fun parseJsonToArrayClassifica(jsonString: String): ArrayList<UtentePunteggio> {
            val gson = Gson()
            return gson.fromJson(
                jsonString,
                object : com.google.gson.reflect.TypeToken<ArrayList<UtentePunteggio>>() {}.type
            )
        }

        fun parseJsonToArrayGiornate(jsonString: String): ArrayList<GiornataPunteggio> {
            val gson = Gson()
            return gson.fromJson(
                jsonString,
                object : com.google.gson.reflect.TypeToken<ArrayList<GiornataPunteggio>>() {}.type
            )
        }


        fun parseJsonToArrayStatistica(jsonString: String): ArrayList<GiocatoreStatistiche> {
            val gson = Gson()
            return gson.fromJson(
                jsonString,
                object : com.google.gson.reflect.TypeToken<ArrayList<GiocatoreStatistiche>>() {}.type
            )
        }
        fun parseJsonToArrayGiocpunt(jsonString: String): ArrayList<GiocPunt> {
            val gson = Gson()
            return gson.fromJson(
                jsonString,
                object : com.google.gson.reflect.TypeToken<ArrayList<GiocPunt>>() {}.type
            )
        }

        fun parseJsonToArrayGiocatori(jsonString: String): ArrayList<GiocatoreFormazione> {
            val gson = Gson()
            return gson.fromJson(
                jsonString,
                object : com.google.gson.reflect.TypeToken<ArrayList<GiocatoreFormazione>>() {}.type
            )
        }


        fun parseJsonToArrayPiloti(jsonString: String): ArrayList<Pilota> {
            val gson = Gson()
            return gson.fromJson(
                jsonString,
                object : com.google.gson.reflect.TypeToken<ArrayList<Pilota>>() {}.type
            )
        }

    }
}