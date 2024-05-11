package com.example.dreamteammanager.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Lega

class LegheAdapter(val data: ArrayList<Lega>) : RecyclerView.Adapter<LegheAdapter.MyViewHolder>()
{
    var onClickListener:SetOnClickListener?=null
    interface SetOnClickListener{
        fun onclick(position: Int,lega: Lega)

    }
    class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val nomelega = row.findViewById<TextView>(R.id.nomeLega)
        val numeropartecipanti=row.findViewById<TextView>(R.id.numeroPartecipanti)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout: View

        layout = LayoutInflater.from(parent.context).inflate(R.layout.item_view,
            parent, false)


        return MyViewHolder(layout)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context=holder.row.context
        holder.nomelega.text=data[position].name
        holder.numeropartecipanti.text="${data[position].numeropartecipanti} partecipanti"
    }
    override fun getItemCount(): Int = data.size
    fun setonclick(onClickListener:SetOnClickListener){
        this.onClickListener=onClickListener
    }

}
