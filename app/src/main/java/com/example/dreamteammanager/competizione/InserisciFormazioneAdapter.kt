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
import com.example.dreamteammanager.viewmodel.ImagesVM

class InserisciFormazioneAdapter(
    val data: ArrayList<GiocatoreFormazione>,
    val imagesVM: ImagesVM,
) :
    RecyclerView.Adapter<InserisciFormazioneAdapter.MyViewHolder>() {
    var onClickListener: SetOnClickListener? = null




    interface SetOnClickListener {
        fun onClick(position: Int, utente: GiocatoreFormazione)
    }

    class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val nome = row.findViewById<TextView>(R.id.nome)
        val immagine = row.findViewById<ImageView>(R.id.profileimage)
        val ruolo = row.findViewById<TextView>(R.id.roleTextView)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout: View

        layout = LayoutInflater.from(parent.context).inflate(
            R.layout.inserisci_formazione_item,
            parent, false
        )
        return MyViewHolder(layout)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context = holder.row.context
        holder.nome.text = data[position].nome
        holder.ruolo.text = "${data[position].ruolo}"
        imagesVM.getProfilePic(context, data[position].id, holder.immagine, true)
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