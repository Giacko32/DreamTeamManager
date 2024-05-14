package com.example.dreamteammanager.retrofit

import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.GET

interface UserAPI {
    @GET(HOTELS_URI)
    fun getHotels(): Call<JsonArray>

    companion object {
        const val HOTELS_URI = "pwm/hotels"
        const val BASE_URL = "http://147.163.214.223:9000"
    }
}