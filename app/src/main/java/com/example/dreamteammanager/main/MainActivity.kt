package com.example.dreamteammanager.main

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import androidx.fragment.app.FragmentManager.BackStackEntry
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.main.MainFragment
import com.example.dreamteammanager.R
import com.example.dreamteammanager.auth.AccessActivity
import com.example.dreamteammanager.classi.Lega
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.databinding.ActivityMainBinding
import com.example.dreamteammanager.databinding.DialogInvitiBinding
import com.example.dreamteammanager.lega.PartecipantiAdapter
import com.example.dreamteammanager.viewmodel.ImagesVM
import com.example.dreamteammanager.viewmodel.SharedPreferencesManager
import com.example.dreamteammanager.viewmodel.UserViewModel


class MainActivity : AppCompatActivity() {
    private val userviewModel: UserViewModel by viewModels()
    private val imagesVM: ImagesVM by viewModels()
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        intent.getParcelableExtra("user", Utente::class.java)?.let { userviewModel.setUtente(it) }
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
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.option_1 -> {
                    val myfragment = supportFragmentManager.findFragmentByTag("PROFILO")
                    if (myfragment == null) {
                        supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            replace<ProfileFragment>(R.id.fragmentContainerView, "PROFILO")
                            addToBackStack("profilo")
                        }
                    }
                }

                R.id.option_2 -> {
                    userviewModel.logout()
                    val logoutintent = Intent(this, AccessActivity::class.java)
                    startActivity(logoutintent)
                    finish()
                }

                R.id.option_3 -> {
                    val invitiDialog = Dialog(this)
                    invitiDialog.setContentView(R.layout.dialog_inviti)
                    userviewModel.getInvitiUtente()
                    val rcview = invitiDialog.findViewById<RecyclerView>(R.id.recviewInviti)
                    userviewModel.InvitiOttenuti.observe(this) {
                        if (it == true) {

                            val adapter =
                                LegheAdapter(userviewModel.listaInviti.value!!, imagesVM, true)
                            rcview.adapter = adapter
                            userviewModel.resetInvitiOttenuti()
                            invitiDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            rcview.layoutManager =
                                LinearLayoutManager(this)
                            invitiDialog.show()
                        }
                    }


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
        SharedPreferencesManager.saveString("user", "")
    }

}



