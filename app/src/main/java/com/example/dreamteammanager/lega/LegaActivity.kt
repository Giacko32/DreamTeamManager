package com.example.dreamteammanager.lega

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.dreamteammanager.R
import com.example.dreamteammanager.auth.LoginFragment
import com.example.dreamteammanager.classi.Lega
import com.example.dreamteammanager.databinding.ActivityLegaBinding
import com.example.dreamteammanager.viewmodel.SharedPreferencesManager
import com.example.dreamteammanager.viewmodel.SingleLegaVM
import com.example.dreamteammanager.viewmodel.parseModelToJson

class LegaActivity : AppCompatActivity() {

    lateinit var binding: ActivityLegaBinding
    private val singleLegaVM:SingleLegaVM by viewModels()
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
        if(intent.getParcelableExtra("lega",Lega::class.java)!=null){
        singleLegaVM.setlega(intent.getParcelableExtra("lega",Lega::class.java)!!)
        }
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<LegaView>(R.id.legafragmentcontainer)
        }


    }
}