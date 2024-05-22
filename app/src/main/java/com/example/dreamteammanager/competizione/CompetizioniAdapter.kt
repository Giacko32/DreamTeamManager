package com.example.dreamteammanager.competizione

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Competizione
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.lega.PartecipantiAdapter

class CompetizioniAdapter(val data: ArrayList<Competizione>) : RecyclerView.Adapter<CompetizioniAdapter.MyViewHolder>() {

    var onClickListener: SetOnClickListener? = null

    interface SetOnClickListener {
        fun onClick(position: Int, competizione: Competizione)
    }

    class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val nomecompetizione = row.findViewById<TextView>(R.id.nomecomp)
        val immagine = row.findViewById<ImageView>(R.id.logocomp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout: View

        layout = LayoutInflater.from(parent.context).inflate(
            R.layout.competizioni_listview,
            parent, false
        )
        return MyViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context = holder.row.context
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, data[position])
        }
    }

    override fun getItemCount(): Int = data.size

    fun setonclick(onClickListener: CompetizioniAdapter.SetOnClickListener) {
        this.onClickListener = onClickListener
    }

}