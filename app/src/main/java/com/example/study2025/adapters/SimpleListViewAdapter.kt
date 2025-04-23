package com.example.study2025.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.study2025.R

class SimpleListViewAdapter(
    private val context: Context,
    private val items: List<String>
): BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(p0: Int): Any = items[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Reuse old view if possible (saves memory & improves performance)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.listview_item, parent, false)

        // Find the TextView inside our item layout
        val textView = view.findViewById<TextView>(R.id.tv)

        // Set the text for this item
        textView.text = items[position]

        // Give this view back to the ListView
        return view
    }
}