package com.example.myapplication.recyclerview

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.myapplication.MyLog
import com.example.myapplication.R
import com.example.myapplication.data.GridDataClass

class CustomRecyclerViewAdapter(private var items: List<GridDataClass>) :
    RecyclerView.Adapter<CustomRecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomRecyclerViewHolder {
        MyLog.funcStart()
        val view=LayoutInflater.from(parent.context).inflate(R.layout.items_view,parent,false)
        return CustomRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        MyLog.funcStart()
        return items.size
    }

    override fun onBindViewHolder(holder: CustomRecyclerViewHolder, position: Int) {
        MyLog.funcStart()
        val item = items[position]
        holder.tvName.text = item.productName
        holder.tvDes.text = item.productDes
        holder.itemView.setOnClickListener { }
    }

    fun setItems(newItems:List<GridDataClass>){
        items=newItems
        notifyDataSetChanged()
    }

}