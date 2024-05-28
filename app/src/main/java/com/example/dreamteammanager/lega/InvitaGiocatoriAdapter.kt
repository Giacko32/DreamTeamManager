package com.example.dreamteammanager.lega

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.viewmodel.ImagesVM

class InvitaGiocatoriAdapter(val data: ArrayList<Utente>, val imagesVM: ImagesVM) : RecyclerView.Adapter<InvitaGiocatoriAdapter.MyViewHolder>() {
    class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val nomeutente = row.findViewById<TextView>(R.id.username)
        val immagine = row.findViewById<ImageView>(R.id.profileimage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout: View

        layout = LayoutInflater.from(parent.context).inflate(
            R.layout.partecipante_item,
            parent, false
        )
        return MyViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context = holder.row.context
        imagesVM.getProfilePic(context, data[position].id, holder.immagine)
        holder.nomeutente.text = data[position].username
    }

    override fun getItemCount(): Int = data.size

}