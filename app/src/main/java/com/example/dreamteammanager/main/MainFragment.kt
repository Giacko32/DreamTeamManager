package com.example.dreamteammanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.dreamteammanager.databinding.FragmentMainBinding
import com.example.dreamteammanager.main.CreaLegaFragment


class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCreateLeague.setOnClickListener{
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            fragmentManager.commit {
                setReorderingAllowed(true)
                replace<CreaLegaFragment>(R.id.fragmentContainerView)
                addToBackStack("main_to_crealega")
            }

        }

    }


}