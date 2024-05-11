package com.example.dreamteammanager.viewmodel

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import com.example.dreamteammanager.classi.MyDatabase
import com.example.dreamteammanager.classi.Utente

@Dao
interface UtenteDao {
    @Query("SELECT * FROM utenti")
    fun getUtente(): Utente?
    @Insert
    fun insert(utente: Utente)
    @Delete
    fun delete(utente: Utente)
}
class UserViewModel(application: Application): AndroidViewModel(application) {
    private val db= Room.databaseBuilder(
        application,
        MyDatabase::class.java, "my_database"
    )
        .build()
    private val _user= MutableLiveData<Utente?>()
    val user: LiveData<Utente?>
    get() = _user
    init {
        _user.value=null
    }
    fun getUtente(){
        _user.value=db.utenteDao().getUtente()
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
        db.utenteDao().insert(utente)
        _user.value=utente
    }
    fun failogin(username:String,password:String):Boolean{
        if(username.isNotEmpty()&&password.isNotEmpty()){
        //Fare login con DB remoto
        if(flagRicordami.value == true){
            insert(Utente(1,username,password,"suca"))
        }
        else {
            _user.value = Utente(1,username,password,"suca")
        }
            return true
        }
        return false
    }
    fun logout(){
        _user.value=null
        if (flagRicordami.value==false){
            db.utenteDao().delete(user.value!!)
        }
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
            Log.d("CODICE", "recuperaCredenziali: $codice ed email inviata")
            //Invia mail con il codice
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