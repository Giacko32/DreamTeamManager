package com.example.dreamteammanager.lega

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.dreamteammanager.R
import com.example.dreamteammanager.auth.LoginFragment
import com.example.dreamteammanager.databinding.ActivityLegaBinding

class LegaActivity : AppCompatActivity() {

    lateinit var binding: ActivityLegaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLegaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<LegaView>(R.id.legafragmentcontainer)
        }

        //creare viewmodel con lega da intent extra
    }
}