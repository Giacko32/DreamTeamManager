package com.example.dreamteammanager.retrofit

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserAPI {
    @GET("pwm/username/{username}/pass/{password}")
    fun getuser(@Path("username")username:String,
                @Path("password")password:String): Call<JsonObject>
    @GET("/username/{username}/email/{email}")
    fun checkDisponibilita(@Path("username")username:String,
                           @Path("email")email:String): Call<JsonArray>
    @POST("username/{username}/pass/{password}/email/{email}")
    fun registeruser(@Path("username")username:String,
                     @Path("password")password:String,
                     @Path("email")email:String): Call<JsonObject>


    companion object {
        const val BASE_URL = "http://192.168.119.165:9000"
    }
}