package com.example.dreamteammanager

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.fragment.app.add

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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

                }
            }
            true
        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        popup.show()
    }


}


