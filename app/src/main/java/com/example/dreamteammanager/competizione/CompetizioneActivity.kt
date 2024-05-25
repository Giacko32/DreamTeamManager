package com.example.dreamteammanager.competizione

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Competizione
import com.example.dreamteammanager.databinding.FragmentCompetizioneViewBinding
import com.example.dreamteammanager.viewmodel.CompetizioniVM

class CompetizioneActivity : AppCompatActivity() {
    private val compVModel: CompetizioniVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_competizione)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        intent.getParcelableExtra("competizione",Competizione::class.java)
            ?.let { compVModel.setCompetizione(it) }
        intent.getIntExtra("idamm",0).let{compVModel.setidamm(it)}

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<CompetizioneViewFragment>(R.id.competizioni_cont_view)
        }
    }
}