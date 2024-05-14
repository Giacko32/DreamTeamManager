package com.example.dreamteammanager.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.dreamteammanager.main.MainFragment
import com.example.dreamteammanager.R
import com.example.dreamteammanager.auth.AccessActivity
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.databinding.ActivityMainBinding
import com.example.dreamteammanager.viewmodel.UserViewModel


class MainActivity : AppCompatActivity() {
    private val userviewModel: UserViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        intent.getParcelableExtra("user",Utente::class.java)?.let { Log.d("User:", it.toString()) }

        binding.menuIcon.setOnClickListener { v: View ->
            showMenu(v, R.menu.menu_main_activity)
        }

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<MainFragment>(R.id.fragmentContainerView)
        }

    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(v.context!!, v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener {item: MenuItem? ->
            when(item!!.itemId) {
                R.id.option_1 -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<ProfileFragment>(R.id.fragmentContainerView)
                        addToBackStack("profilo")
                    }
                }
                R.id.option_2 -> {
                    userviewModel.logout(intent.getBooleanExtra("flag", false))
                    val logoutintent = Intent(this, AccessActivity::class.java)
                    startActivity(logoutintent)
                    finish()
                }
            }
            true
        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        popup.setForceShowIcon(true)
        popup.show()
    }

    override fun onStop() {
        super.onStop()
        userviewModel.logout(intent.getBooleanExtra("flag", false))
    }

    override fun onDestroy() {
        super.onDestroy()
        userviewModel.logout(intent.getBooleanExtra("flag", false))
    }



}
@Suppress("DEPRECATION")
inline fun <reified T: Parcelable> Bundle.getParcelableCustom(identifierParameter: String): T? {
    return if (Build.VERSION.SDK_INT >= 33) {
        this.getParcelable(identifierParameter, T::class.java)
    } else {
        this.getParcelable(identifierParameter)
    }
}


