package com.example.dreamteammanager.lega

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.databinding.FragmentRichiesteBinding
import com.example.dreamteammanager.viewmodel.SingleLegaVM
import kotlin.math.sin


class RichiesteFragment : Fragment() {
   lateinit var binding: FragmentRichiesteBinding
   private val singleLegaVM: SingleLegaVM by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        singleLegaVM.getrichiedenti()
        singleLegaVM.scaricando2.observe(viewLifecycleOwner){
            if(it==true){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                val adapter = PartecipantiAdapter(singleLegaVM.richiedenti.value!!,false)
                binding.rv.layoutManager=LinearLayoutManager(context)
                binding.rv.adapter=adapter
                binding.progressBar.visibility = View.GONE
                singleLegaVM.resetscaricando2()
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