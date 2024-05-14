package com.example.dreamteammanager.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dreamteammanager.classi.Utente
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import org.json.JSONObject
import java.io.File
import java.io.FileReader
import java.io.FileWriter


class UserViewModel(application: Application): AndroidViewModel(application) {
    init {
        SharedPreferencesManager.init(application)
    }
    fun parseJsonToModel(jsonString: String): Utente {
        val gson = Gson()
        return gson.fromJson(jsonString, object : com.google.gson.reflect.TypeToken<Utente>() {}.type)
    }
    fun parseModelToJson(utente: Utente): String {
        val gson = Gson()
        return gson.toJson(utente)
    }


    object SharedPreferencesManager {

        private const val PREF_NAME = "MySharedPrefs"
        private lateinit var sharedPreferences: SharedPreferences

        fun init(context: Context) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }

        fun saveString(key: String, value: String) {
            sharedPreferences.edit().putString(key, value).apply()
        }

         fun getString(key: String, defaultValue: String): String {
            return sharedPreferences.getString(key, defaultValue) ?: defaultValue
        }
    }
    private val _user= MutableLiveData<Utente?>()
    val user: LiveData<Utente?>
    get() = _user
    init {
        val jsonString = SharedPreferencesManager.getString("utente", "")
        if (jsonString.isNotEmpty()) {
            val utente = parseJsonToModel(jsonString)
            _user.value = utente
        }else{
            _user.value=null
        }
        Log.d("USER", "user: ${user.value}")
    }
    private val _flagRicordami=MutableLiveData<Boolean>()
    val flagRicordami:LiveData<Boolean>
    get() = _flagRicordami
    init {
        _flagRicordami.value=false
    }
    fun updateFlag(isChecked: Boolean) {
        _flagRicordami.value = isChecked
        Log.d("FLAG", "updateFlag: $isChecked")

    }
    fun insert(utente: Utente){
        val jsonString = parseModelToJson(utente)
        SharedPreferencesManager.saveString("utente", jsonString)
    }
    fun failogin(username:String,password:String):Boolean{
        if(username.isNotEmpty()&&password.isNotEmpty()){
        //Fare login con DB remoto
        if(flagRicordami.value == true){
            insert(Utente(1,username,password,""))
        }
            _user.value = Utente(1,username,password,"")

            return true
        }
        return false
    }
    fun logout(flag:Boolean){
        if (!flag){
            SharedPreferencesManager.saveString("utente", "")
        }
        _user.value=null
    }
    fun registrati(username:String,password:String,email:String):String{
        if(username.isNotEmpty()&&password.isNotEmpty()&&email.isNotEmpty()) {
            if(password.length>=8&&password.length<=25){
                if(isValidEmail(email)){
                    //registra nel DB remoto
                    return "Registrazione avvenuta con successo"
                }
                else{
                return "Email non valida"
                }
            }
            else{
                    return "Password non formattata correttamente"
                }
        }
        else{
              return "Campi lasciati vuoti"
            }
    }
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private val _codice=MutableLiveData<Int>()
    val codice:LiveData<Int>
    get() = _codice
    init {
        _codice.value=0
    }
    private fun generatecodice(){
        _codice.value=(100000..999999).random()
    }
    fun recuperaCredenziali(email: String):Boolean{
        if(email.isNotEmpty()&&isValidEmail(email)){
            generatecodice()
            Log.d("CODICE", "recuperaCredenziali: ${codice.value} ed email inviata")
            //sendEmail(email,getApplication())
            return true
        }
        else{
            Log.d("CODICE", "recuperaCredenziali: email non valida")
            return false
        }
    }
    fun controllacodice(codice:Int):Boolean{
        if(codice==this.codice.value){
            return true
        }
        return false
    }
    fun cambiapassword(password:String,confirm:String):Boolean{
        if(password.isNotEmpty()&&password.length>=8&&password.length<=25){
            if(password==confirm){
                //Cambia password in DB remoto
                return true
            }
        }
        return false
    }

}
