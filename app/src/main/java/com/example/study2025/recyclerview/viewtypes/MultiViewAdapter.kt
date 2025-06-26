package com.example.study2025.recyclerview.viewtypes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.study2025.R

class MultiViewAdapter(val list: List<ListItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val HEADER_VIEW_TYPE = 0
        private const val TEXT_VIEW_TYPE = 1
        private const val IMAGE_VIEW_TYPE = 2
    }

    override fun getItemViewType(position: Int): Int = when(list[position]) {
        is ListItem.Header -> HEADER_VIEW_TYPE
        is ListItem.ImageItem -> IMAGE_VIEW_TYPE
        is ListItem.TextItem -> TEXT_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            HEADER_VIEW_TYPE -> HeaderViewHolder(inflater.inflate(R.layout.item_header, parent, false))
            TEXT_VIEW_TYPE -> TextViewHolder(inflater.inflate(R.layout.item_text, parent, false))
            IMAGE_VIEW_TYPE -> ImageViewHolder(inflater.inflate(R.layout.item_image, parent, false))
            else -> throw IllegalArgumentException("Unknown viewType $viewType")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item = list[position]) {
            is ListItem.TextItem -> (holder as TextViewHolder).bind(item)
            is ListItem.ImageItem -> (holder as ImageViewHolder).bind(item)
            ListItem.Header -> TODO()
        }
    }

}

class HeaderViewHolder(val view: View): RecyclerView.ViewHolder(view)

class TextViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    private val textView = view.findViewById<TextView>(R.id.textView)

    fun bind(item: ListItem.TextItem) {
        textView.text = item.text;
    }
}

class ImageViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    private val imageView = view.findViewById<ImageView>(R.id.imageView)

    fun bind(item: ListItem.ImageItem){
        imageView.setImageResource(item.imgId)
    }
}