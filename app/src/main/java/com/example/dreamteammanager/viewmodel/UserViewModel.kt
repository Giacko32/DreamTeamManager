package com.example.dreamteammanager.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
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
class UserViewModel(application: Application,db: MyDatabase):AndroidViewModel(application) {
    private val db=db
    private val _user= MutableLiveData<Utente?>()
    val user: LiveData<Utente?>
    get() = _user
    init {
        val utente=db.utenteDao().getUtente()
        if (utente!=null){
            _user.value=utente
            failogin(user.value!!.username,user.value!!.password)
        }else{
            _user.value=null
        }

    }
    private val _flagRicordami=MutableLiveData<Boolean>()
    val flagRicordami:LiveData<Boolean>
    get() = _flagRicordami
    init {
        _flagRicordami.value=false
    }
    fun updateFlag() {
        if (flagRicordami.value==true) {
            _flagRicordami.value=false
        }
        else {
            _flagRicordami.value=true
        }
    }
    fun insert(utente: Utente){
        db.utenteDao().insert(utente)
        _user.value=utente
    }
    fun failogin(username:String,password:String){
        //Fare login con DB remoto
        if(flagRicordami.value == true){
            insert(Utente(1,username,password,"suca"))
        }
        else {
            _user.value = Utente(1,username,password,"suca")
        }
    }
    fun logout(){
        _user.value=null
        if (flagRicordami.value==false){
            db.utenteDao().delete(user.value!!)
        }
    }
    fun registrati(username:String,password:String,email:String):Boolean{
        if(username.isNotEmpty()&&password.isNotEmpty()&&email.isNotEmpty()&&isValidEmail(email)&&password.length>=8&&password.length<=25){
            //Registrazione con DB remoto
            return true
        }else{
            return false
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}