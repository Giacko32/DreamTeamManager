package com.example.dreamteammanager.lega

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.R
import com.example.dreamteammanager.databinding.FragmentInvitaBinding
import com.example.dreamteammanager.databinding.FragmentMainBinding
import com.example.dreamteammanager.viewmodel.ImagesVM
import com.example.dreamteammanager.viewmodel.SingleLegaVM


class InvitaFragment : Fragment() {
    lateinit var binding: FragmentInvitaBinding
    private val singleLegaVM: SingleLegaVM by activityViewModels()
    private val imagesVM: ImagesVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInvitaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        singleLegaVM.getutentiinivito()
        singleLegaVM.utentiinvito.observe(viewLifecycleOwner){
            if(it==true){
                var adapter:PartecipantiAdapter
                val rvInvita = binding.recyclerViewInvita
                binding.searchIcon.setOnClickListener{
                    val testo=binding.searched.text.toString()
                    singleLegaVM.setinvitatiFiltrato(testo)
                    adapter=PartecipantiAdapter(singleLegaVM.invitatifiltrato.value!!,false,imagesVM,null)
                    rvInvita.adapter = adapter
                }
                rvInvita.layoutManager = LinearLayoutManager(requireActivity())
                adapter=PartecipantiAdapter(singleLegaVM.invitati.value!!,false,imagesVM,null)
                rvInvita.adapter = adapter
                singleLegaVM.resetutentiinvito()
                binding.progressBar.visibility=View.GONE
            }else if(it==false){
                binding.progressBar.visibility=View.VISIBLE
            }
        }
    }


}