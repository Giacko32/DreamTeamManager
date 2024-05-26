package com.example.dreamteammanager.competizione

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.GiocatoreStatistiche
import com.example.dreamteammanager.viewmodel.ImagesVM

class StatisticheAdapter(
    val data: ArrayList<GiocatoreStatistiche>,
    val imagesVM: ImagesVM,
) :
    RecyclerView.Adapter<StatisticheAdapter.MyViewHolder>() {
    var onClickListener: SetOnClickListener? = null




    interface SetOnClickListener {
        fun onClick(position: Int, utente: GiocatoreStatistiche)
    }

    class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val nome = row.findViewById<TextView>(R.id.name)
        val immagine = row.findViewById<ImageView>(R.id.profileimage)
        val voti = row.findViewById<TextView>(R.id.voti)
        val ruolo = row.findViewById<TextView>(R.id.ruolo)

    }
    init {
        // Ordina i dati in modo decrescente per punteggio
        sortData()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout: View

        layout = LayoutInflater.from(parent.context).inflate(
            R.layout.statistiche_item,
            parent, false
        )
        return MyViewHolder(layout)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context = holder.row.context
        holder.nome.text = data[position].nome
        holder.voti.text = "Media Voti: ${data[position].mediaVoti}"
        holder.ruolo.text = "${data[position].ruolo}"
        imagesVM.getProfilePic(context, data[position].id, holder.immagine)
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, data[position])
            notifyDataSetChanged()
        }

    }
    override fun getItemCount(): Int = data.size
    fun setonclick(onClickListener: SetOnClickListener) {
        this.onClickListener = onClickListener
    }

    fun sortData() {
        data.sortByDescending { it.mediaVoti }
    }

}