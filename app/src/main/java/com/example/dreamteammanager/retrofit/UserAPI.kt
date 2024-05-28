package com.example.dreamteammanager.retrofit

import com.example.dreamteammanager.viewmodel.CompezioneLegaPart
import com.example.dreamteammanager.viewmodel.Emailcode
import com.example.dreamteammanager.viewmodel.ModifyCredenzialiProfile
import com.example.dreamteammanager.viewmodel.UtenteLega
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
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
    fun insertImage(@Body userimage: JsonObject): Call<JsonObject>

    @PUT("pwm/changeProfileData")
    fun changeCredenzialiProfile(
        @Body modifyCredenzialiProfile: JsonObject
    ): Call<JsonObject>

    @GET("pwm/getPartecipanti/{idlega}")
    fun getPartecipanti(@Path("idlega") idlega: Int): Call<JsonArray>

    @GET("pwm/getrichiedenti/{idlega}")
    fun getrichiedenti(@Path("idlega") idlega: Int): Call<JsonArray>

    @PUT("pwm/accettautente")
    fun accettautente(@Body utenteLega: JsonObject): Call<JsonObject>

    @PUT("pwm/rifiutautente")
    fun rifiutautente(@Body utenteLega: JsonObject): Call<JsonObject>

    @GET("pwm/getnonpartecipanti/{idlega}")
    fun getnonpartecipanti(@Path("idlega") idlega: Int): Call<JsonArray>

    @GET("pwm/getInviti/{id}")
    fun getInvitiUtente(@Path("id") id: Int): Call<JsonArray>
    @POST("pwm/invitautente")
    fun invitautente(@Body utenteLega: JsonObject): Call<JsonObject>
    @POST("pwm/creacomp")
    fun creacomp(@Body compezioneLegaPart: JsonObject): Call<JsonObject>
    @GET("pwm/getcomp/{idlega}")
    fun getcomp(@Path("idlega")idlega: Int):Call<JsonArray>
    @GET("pwm/getpart/{idcomp}")
    fun getpart(@Path("idcomp")idcomp: Int):Call<JsonArray>
    @GET("pwm/getgiornatedacalcolare/{idcomp}")
    fun getgiornatedacalcolare(@Path("idcomp")idcomp: Int):Call<JsonArray>
    @POST("pwm/calcolagiornata")
    fun calcolagiornata(@Body compezionegiornata: JsonObject):Call<JsonObject>

    @GET("pwm/getultimegiornate/{id_utente}")
    fun getultimegiornate(@Path("id_utente")id_utente: Int):Call<JsonArray>

    @GET("pwm/getultimegiornateverificate/{id_utente}")
    fun getgiornateverificate(@Path("id_utente")id_utente: Int):Call<JsonArray>

    @PUT("pwm/aggiornaVerifiedGiornata")
    fun aggiornaVerifiedGiornata(@Body UtenteCompGiornata: JsonObject): Call<JsonObject>

    @GET("pwm/getAccettazioniNotify/{id_utente}")
    fun getAccettazioniNotify(@Path("id_utente")id_utente: Int):Call<JsonArray>

    @GET("pwm/getClassifica/{id_cmp}")
    fun getClassifica(@Path("id_cmp")id_cmp: Int):Call<JsonArray>

    @GET("pwm/getStatistica/{sport}")
    fun getStatistica(@Path("sport")sport: String):Call<JsonArray>
    @GET("pwm/getGiocatoriDisp/{sport}/{idcmp}")
    fun getGiocatoriDisp(@Path("sport")sport: String, @Path("idcmp")idcmp: Int):Call<JsonArray>
    @POST("pwm/insertgioc")
    fun insertgioc(@Body UtenteCompGioc: JsonObject): Call<JsonObject>

    @GET("pwm/getRosa/{id_utente}/{id_comp}/{sport}")
    fun getRosa(@Path("sport")sport: String, @Path("id_comp") idcomp: Int, @Path("id_utente") id_utente: Int):Call<JsonArray>

    @POST("pwm/insertFormazione")
    fun insertFormazione(@Body Formazione: JsonObject): Call<JsonObject>
    @GET("pwm/getgiornatedacalcolare/{idcomp}/{idutente}")
    fun getgiornate(@Path("idcomp")idcomp: Int,@Path("idutente")idutente: Int):Call<JsonArray>
    @GET("pwm/getgiornatecalmie/{idcomp}/{idutente}")
    fun getgiornatecalcmie(@Path("idcomp")idcomp: Int,@Path("idutente")idutente: Int):Call<JsonArray>
    @GET("pwm/getgiornatecalgioc/{idcomp}/{idutente}/{giornata}")
    fun getgiornatecalgioc(@Path("idcomp")idcomp: Int,@Path("idutente")idutente: Int,@Path("giornata")giornata: Int):Call<JsonArray>


    companion object {
        const val BASE_URL = "http://192.168.133.165:9000"
    }
}