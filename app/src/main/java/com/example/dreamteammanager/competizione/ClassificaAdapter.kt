package com.example.dreamteammanager.competizione


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamteammanager.R
import com.example.dreamteammanager.classi.UtentePunteggio
import com.example.dreamteammanager.viewmodel.ImagesVM

class ClassificaAdapter(
    val data: ArrayList<UtentePunteggio>,
    val imagesVM: ImagesVM,
) :
    RecyclerView.Adapter<ClassificaAdapter.MyViewHolder>() {
    var onClickListener: SetOnClickListener? = null



    interface SetOnClickListener {
        fun onClick(position: Int, utente: UtentePunteggio)
    }

    class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val nomeutente = row.findViewById<TextView>(R.id.username)
        val immagine = row.findViewById<ImageView>(R.id.profileimage)
        val punteggio = row.findViewById<TextView>(R.id.score)
        val posizione = row.findViewById<TextView>(R.id.ranking)

    }
    init {
        sortData()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout: View

        layout = LayoutInflater.from(parent.context).inflate(
            R.layout.partecipante_classifica_item,
            parent, false
        )
        return MyViewHolder(layout)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context = holder.row.context
        holder.nomeutente.text = data[position].username
        holder.punteggio.text = "Score: ${data[position].punteggio}"
        holder.posizione.text = "${position + 1}°"
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
        data.sortByDescending { it.punteggio }
    }

}