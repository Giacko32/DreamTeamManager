package com.example.dreamteammanager.retrofit

import com.example.dreamteammanager.viewmodel.Emailcode
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserAPI {
    @GET("pwm/username/{username}/pass/{password}")
    fun getuser(
        @Path("username") username: String,
        @Path("password") password: String
    ): Call<JsonObject>

    @GET("pwm/username/{username}/email/{email}")
    fun checkDisponibilita(
        @Path("username") username: String,
        @Path("email") email: String
    ): Call<JsonArray>

    @POST("pwm/user")
    fun registeruser(@Body user: JsonObject): Call<JsonObject>

    @GET("pwm/recovery/{email}")
    fun verificaemail(@Path("email") email: String): Call<JsonObject>

    @POST("pwm/recovery")
    fun inviamail(@Body emailcode: Emailcode): Call<JsonObject>

    @PUT("pwm/change")
    fun cambiapassword(@Body user: JsonObject): Call<JsonObject>

    @GET("pwm/mieleghe/{id}")
    fun scaricamieleghe(@Path("id") id: Int): Call<JsonArray>

    @GET("pwm/tutteleghe/{id}")
    fun scaricatutteleghe(@Path("id") id: Int): Call<JsonArray>

    @POST("pwm/insertlega")
    fun insertlega(@Body lega: JsonObject): Call<JsonObject>

    @POST("pwm/insertutente")
    fun insertutente(@Body utente: JsonObject): Call<JsonObject>

    @POST("pwm/richiestadipart")
    fun richiestadipart(@Body utente: JsonObject): Call<JsonObject>

    @GET("pwm/checkrequest/{idlega}/{idutente}")
    fun checkrequest(
        @Path("idlega") idlega: Int,
        @Path("idutente") idutente: Int
    ): Call<JsonArray>

    @POST("pwm/insertImage")
    fun insertImage(@Body utente: JsonObject): Call<JsonObject>

    @GET("pwm/requireImage/{id}")
    fun getProfileImage(@Path("id") id: Int): Call<JsonObject>

    companion object {
        const val BASE_URL = "http://192.168.133.165:9000"
    }
}