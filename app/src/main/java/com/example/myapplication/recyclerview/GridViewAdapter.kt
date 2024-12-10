package com.example.myapplication.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.myapplication.R
import com.example.myapplication.data.GridDataClass

open class GridViewAdapter(val productList: List<GridDataClass>, val context: Context) :
    BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null

    class ViewHolder {
        lateinit var nameTextView: TextView
        lateinit var desTextView: TextView
    }

    override fun getCount(): Int {
        return productList.size
    }

    override fun getItem(position: Int): Any {
        return productList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            viewHolder = ViewHolder()
            if (layoutInflater == null) {
                layoutInflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            }
            view = layoutInflater!!.inflate(R.layout.items_view, parent, false)
            viewHolder.nameTextView = view.findViewById(R.id.tvname)
            viewHolder.desTextView = view.findViewById(R.id.tvdes)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val product = productList[position]
        viewHolder.nameTextView.text = product.productName
        viewHolder.desTextView.text = product.productDes

        return view
    }

}