package com.example.dreamteammanager.main

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.dreamteammanager.R
import com.example.dreamteammanager.auth.AccessActivity
import com.example.dreamteammanager.classi.Lega
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.databinding.ActivityMainBinding
import com.example.dreamteammanager.lega.LegaActivity
import com.example.dreamteammanager.notification.NotificaAccetazioneLega
import com.example.dreamteammanager.notification.NotificaCaricamentoGiornata
import com.example.dreamteammanager.notification.NotificaInattivita
import com.example.dreamteammanager.viewmodel.ImagesVM
import com.example.dreamteammanager.viewmodel.SharedPreferencesManager
import com.example.dreamteammanager.viewmodel.UserViewModel
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private val userviewModel: UserViewModel by viewModels()
    private val imagesVM: ImagesVM by viewModels()
    lateinit var binding: ActivityMainBinding

    private fun avviaNotifiche() {
        WorkManager.getInstance(this).cancelAllWorkByTag("inactivity_notification")
        WorkManager.getInstance(this).cancelAllWorkByTag("giornata_notification")
        WorkManager.getInstance(this).cancelAllWorkByTag("accettazione_notification")
        val notificationInattivita =
            PeriodicWorkRequestBuilder<NotificaInattivita>(24, TimeUnit.HOURS).setInitialDelay(
                24,
                TimeUnit.HOURS
            )
                .addTag("inactivity_notification")
                .build()
        val notificationGiornata =
            PeriodicWorkRequestBuilder<NotificaCaricamentoGiornata>(
                15,
                TimeUnit.MINUTES
            ).setInitialDelay(
                15,
                TimeUnit.SECONDS
            )
                .addTag("giornata_notification")
                .build()

        val notificationAccettazione = PeriodicWorkRequestBuilder<NotificaAccetazioneLega>(
            15,
            TimeUnit.MINUTES
        ).setInitialDelay(15, TimeUnit.SECONDS).addTag("accettazione_notification").build()

        WorkManager.getInstance(this).enqueue(notificationInattivita)
        WorkManager.getInstance(this).enqueue(notificationGiornata)
        WorkManager.getInstance(this).enqueue(notificationAccettazione)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                avviaNotifiche()
            }
        }

    private fun setupPermission() {
        val permission =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            avviaNotifiche()
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
            val alertDialog = AlertDialog.Builder(
                this,
                androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog_Alert
            ).create()
            alertDialog.setTitle("ERRORE NOTIFICHE")
            alertDialog.setMessage("L'applicazione non ha i permessi necessari per inviare notifiche e non potrà avvisarti del caricamento delle giornate")
            alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE, "Ho cambiato idea"
            ) { dialog, which ->
                dialog.dismiss()
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            alertDialog.setButton(
                AlertDialog.BUTTON_NEGATIVE, "Non ho cambiato idea"
            ) { dialog, which ->
                dialog.dismiss()
            }
            alertDialog.show()
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(Color.parseColor("#ff5722"))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#ff5722"))
        } else if (permission == PackageManager.PERMISSION_DENIED) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

    }


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

        setupPermission()

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
                            adapter.setonclick(object : LegheAdapter.SetOnClickListener {
                                override fun onClick(position: Int, lega: Lega) {
                                    val InvitaDialog = Dialog(this@MainActivity)
                                    InvitaDialog.setContentView(R.layout.fragment_custom_dialog)
                                    InvitaDialog.findViewById<TextView>(R.id.dialogTitle).setText(
                                        "Vuoi invitare nella lega ${lega.name}?"
                                    )
                                    InvitaDialog.findViewById<Button>(R.id.yesButton)
                                        .setOnClickListener {
                                            userviewModel.accettainvito(
                                                lega.id,
                                                userviewModel.user.value!!.id,
                                                lega.numeropartecipanti
                                            )
                                            InvitaDialog.dismiss()
                                            invitiDialog.dismiss()
                                            userviewModel.accettando.observe(this@MainActivity) {
                                                if (it == false) {
                                                    binding.progressBar.visibility = View.GONE
                                                    userviewModel.resetaccettando()
                                                    val legaintent = Intent(
                                                        this@MainActivity,
                                                        LegaActivity::class.java
                                                    )
                                                    legaintent.putExtra("lega", lega)
                                                    startActivity(legaintent)
                                                } else if (it == true) {
                                                    binding.progressBar.visibility = View.VISIBLE
                                                }
                                            }
                                        }
                                    InvitaDialog.findViewById<Button>(R.id.noButton)
                                        .setOnClickListener {
                                            userviewModel.rifiutainvito(
                                                lega.id,
                                                userviewModel.user.value!!.id
                                            )
                                            InvitaDialog.dismiss()
                                            invitiDialog.dismiss()
                                        }
                                    InvitaDialog.show()
                                }
                            })
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



