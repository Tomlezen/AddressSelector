package com.tlz.addressselector.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.tlz.addressselector.R
import com.tlz.addressselector.model.Name


/**
 *
 * Created by Tomlezen.
 * Date: 2017/7/10.
 * Time: 12:06.
 */
open class DataAdapter<T: Name>(val data: MutableList<T> = mutableListOf()): BaseAdapter() {

    private var selectedPosition = -1

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val convertView: View
        if(p1 == null){
            convertView = LayoutInflater.from(p2?.context).inflate(R.layout.item_layout, p2, false)
        }else{
            convertView = p1
        }
        val nameView: TextView? = convertView.findViewById(R.id.name)
        nameView?.text = data[p0].name
        val selectedView: View? = convertView.findViewById(R.id.choosed_img)
        if (selectedPosition == p0) {
            selectedView?.visibility = View.VISIBLE
        } else {
            selectedView?.visibility = View.GONE
        }

        return convertView
    }

    override fun getItem(p0: Int): Any  = data[p0]

    override fun getItemId(p0: Int) = p0.toLong()

    override fun getCount() = data.size

    fun setData(data: List<T>){
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun setSelectedPosition(position: Int){
        if(position != selectedPosition) {
            selectedPosition = position
            notifyDataSetChanged()
        }
    }

}