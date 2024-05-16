package com.example.collectrack

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.collectrack.models.Category

class CategoryAdapter(context: Context, resource: Int, objects: List<Category>) :
    ArrayAdapter<Category>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false)
        }

        val category = getItem(position)

        val categoryNameTextView = itemView?.findViewById<TextView>(R.id.categoryName)

        categoryNameTextView?.text = category?.name

        return itemView!!
    }
}
