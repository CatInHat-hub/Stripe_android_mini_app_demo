package com.example.myapplication.recyclerview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myapplication.R

class CustomRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvName:TextView=itemView.findViewById(R.id.tvname)
    val tvDes:TextView=itemView.findViewById(R.id.tvdes)
}
