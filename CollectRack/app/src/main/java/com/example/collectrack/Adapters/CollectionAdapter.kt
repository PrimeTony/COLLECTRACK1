package com.example.collectrack

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.collectrack.models.Collection


class CollectionAdapter(
    private val context: Context,
    private val resource: Int,
    private val collections: List<Collection>
) : ArrayAdapter<Collection>(context, resource, collections) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)

        val collection = collections[position]

        val nameTextView: TextView = view.findViewById(R.id.collectionName)
        val descriptionTextView: TextView = view.findViewById(R.id.collectionDescription)
        val categoryTextView: TextView = view.findViewById(R.id.collectionCategory)


        nameTextView.text = collection.name
        descriptionTextView.text = collection.description
        categoryTextView.text = collection.category


        return view
    }
}
