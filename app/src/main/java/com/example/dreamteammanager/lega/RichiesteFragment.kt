package com.example.dreamteammanager.lega

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.databinding.FragmentRichiesteBinding
import com.example.dreamteammanager.viewmodel.ImagesVM
import com.example.dreamteammanager.viewmodel.SharedPreferencesManager
import com.example.dreamteammanager.viewmodel.SingleLegaVM
import com.example.dreamteammanager.viewmodel.parseJsonToLega
import kotlin.math.sin


class RichiesteFragment : Fragment() {
    lateinit var binding: FragmentRichiesteBinding
    private val singleLegaVM: SingleLegaVM by activityViewModels()
    private val imagesVM: ImagesVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        singleLegaVM.getrichiedenti()
        singleLegaVM.scaricando.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else if (it == false) {
                val adapter = PartecipantiAdapter(singleLegaVM.richiedenti.value!!, false, imagesVM)
                binding.rv.layoutManager = LinearLayoutManager(context)
                binding.rv.adapter = adapter
                binding.progressBar.visibility = View.GONE
                singleLegaVM.resetscaricando()
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRichiesteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}