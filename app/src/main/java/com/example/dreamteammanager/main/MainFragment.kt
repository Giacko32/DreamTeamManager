package com.example.dreamteammanager.main

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Lega
import com.example.dreamteammanager.databinding.FragmentMainBinding
import com.example.dreamteammanager.lega.LegaActivity
import com.example.dreamteammanager.viewmodel.SharedPreferencesManager
import com.example.dreamteammanager.viewmodel.legheVM
import com.example.dreamteammanager.viewmodel.parseJsonToModel


class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    private val legheVM:legheVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val utente= parseJsonToModel(
            SharedPreferencesManager.getString(
                "utente",
                ""
            )
        )
        legheVM.scaricaleghe(utente.id)
        val creaLegaDialog = Dialog(requireActivity())
        val IscrizioneDialog = Dialog(requireActivity())

        binding.creaNuovaLegaButton.setOnClickListener {
            creaLegaDialog.setContentView(R.layout.dialog_crea_lega)
            creaLegaDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            creaLegaDialog.findViewById<Button>(R.id.creaLegaButton).setOnClickListener {
                val nome = creaLegaDialog.findViewById<TextView>(R.id.NomeLega).text.toString()
                legheVM.creanuovalega(Lega(0,nome,1,utente.id))
                creaLegaDialog.dismiss()
            }
            creaLegaDialog.show()


        }

        legheVM.scaricando.observe(viewLifecycleOwner){
            if(it==true){
                binding.progressBar.visibility=View.VISIBLE
            }else if(it==false){
                binding.progressBar.visibility=View.GONE
            }
        }

        legheVM.listaLeghe.observe(viewLifecycleOwner){
            val adapter= LegheAdapter(it)
            if(legheVM.mieleghe.value==true){
            adapter.setonclick(object : LegheAdapter.SetOnClickListener{
                override fun onClick(position: Int, lega: Lega){
                    val legaintent = Intent(requireActivity(), LegaActivity::class.java)
                    legaintent.putExtra("lega",lega)
                    startActivity(legaintent)
                }
        })}
            else{
                adapter.setonclick(object : LegheAdapter.SetOnClickListener{
                    override fun onClick(position: Int, lega: Lega){
                        IscrizioneDialog.setContentView(R.layout.fragment_custom_dialog)
                        IscrizioneDialog.findViewById<TextView>(R.id.dialogTitle).setText("Vuoi iscriverti alla lega:"+lega.name)
                        IscrizioneDialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                        IscrizioneDialog.show()

            }})
            }
            binding.recyclerView.layoutManager=LinearLayoutManager(context)
            binding.recyclerView.adapter=adapter
        }
        binding.buttonMyLeagues.setOnClickListener{
            legheVM.setMieleghe(true)
            legheVM.scaricaleghe(utente.id)
        }
        binding.buttonJoinLeague.setOnClickListener{
            legheVM.setMieleghe(false)
            legheVM.scaricaleghe(utente.id)
        }



        }

}