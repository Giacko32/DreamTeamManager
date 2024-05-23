package com.example.dreamteammanager.lega

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.Lega
import com.example.dreamteammanager.classi.Utente
import com.example.dreamteammanager.main.LegheAdapter
import com.example.dreamteammanager.viewmodel.ImagesVM

class PartecipantiAdapter(val data: ArrayList<Utente>, val selectable: Boolean, val imagesVM: ImagesVM, val id_amm: Int?) :
    RecyclerView.Adapter<PartecipantiAdapter.MyViewHolder>() {
    var onClickListener: SetOnClickListener? = null

    interface SetOnClickListener {
        fun onClick(position: Int, utente: Utente)
    }
    class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val nomeutente = row.findViewById<TextView>(R.id.username)
        val immagine = row.findViewById<ImageView>(R.id.profileimage)
        val selector = row.findViewById<CheckBox>(R.id.selector)
        val star = row.findViewById<ImageView>(R.id.administratorStar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout: View

        layout = LayoutInflater.from(parent.context).inflate(
            R.layout.partecipante_item,
            parent, false
        )
        return MyViewHolder(layout)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context = holder.row.context
        holder.nomeutente.text = data[position].username
        if(id_amm != null){
            if(data[position].id == id_amm)
            {
                holder.star.visibility = View.VISIBLE
            }
        }
        imagesVM.getProfilePic(context, data[position].id, holder.immagine)
        if (selectable) {
            holder.selector.visibility = View.VISIBLE
        } else {
            holder.selector.visibility = View.GONE
        }
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