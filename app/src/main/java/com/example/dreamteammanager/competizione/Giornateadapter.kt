package com.example.dreamteammanager.competizione

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.GiornataPunteggio

class Giornateadapter(
    val data: ArrayList<GiornataPunteggio>,val sport:String
) :
    RecyclerView.Adapter<Giornateadapter.MyViewHolder>() {
    var onClickListener: SetOnClickListener? = null

    interface SetOnClickListener {
        fun onClick(position: Int, giornataPunteggio: GiornataPunteggio)
    }

    class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val giornata = row.findViewById<TextView>(R.id.giornataText)
        val punteggio = row.findViewById<TextView>(R.id.Punteggio_Text)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout: View

        layout = LayoutInflater.from(parent.context).inflate(
            R.layout.item_view_giornata,
            parent, false
        )
        return MyViewHolder(layout)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context = holder.row.context
        if (sport=="Serie A"){
        holder.giornata.text = "Giornata ${data[position].giornata}"}
        else{
            holder.giornata.text = "Gara ${data[position].giornata}"
        }
        holder.punteggio.text = data[position].punteggio.toString()
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