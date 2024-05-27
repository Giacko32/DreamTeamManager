package com.example.dreamteammanager.competizione

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.GiocatoreFormazione
import com.example.dreamteammanager.classi.GiocatoreStatistiche
import com.example.dreamteammanager.classi.Pilota
import com.example.dreamteammanager.classi.PilotaGriglia
import com.example.dreamteammanager.viewmodel.ImagesVM

class InserisciGrigliaAdapter(
    val data: List<Pilota>
) :
    RecyclerView.Adapter<InserisciGrigliaAdapter.MyViewHolder>() {
    var onClickListener: SetOnClickListener? = null
    interface SetOnClickListener {
        fun onClick(position: Int, utente: Pilota)
    }

    class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val nome = row.findViewById<TextView>(R.id.nome)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout: View

        layout = LayoutInflater.from(parent.context).inflate(
            R.layout.inserisci_griglia_item,
            parent, false
        )
        return MyViewHolder(layout)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context = holder.row.context
        holder.nome.text = data[position].nome

        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, data[position])
            notifyDataSetChanged()
        }

    }
    override fun getItemCount(): Int = data.size
    fun setonclick(onClickListener: SetOnClickListener) {
        this.onClickListener = onClickListener
    }


}