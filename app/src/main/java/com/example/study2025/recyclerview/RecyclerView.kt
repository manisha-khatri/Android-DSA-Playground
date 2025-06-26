package com.example.study2025.recyclerview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.study2025.R

interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class RVActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rv)

        val rv = findViewById<RecyclerView>(R.id.recyclerView)

        rv.layoutManager = LinearLayoutManager(this)
        val list = listOf("Manisha", "Elon", "Sunder");
        rv.adapter = NameAdapter(
            list,
            object : OnItemClickListener{
                override fun onItemClick(position: Int) {
                    Toast.makeText(this@RVActivity, "This message is at position = " + position, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}

class NameAdapter(
    private val names: List<String>,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<NameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_name, parent, false)
        return NameViewHolder(view)
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        holder.textName.text = names[position]
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int = names.size
}

class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textName: TextView = itemView.findViewById(R.id.textName)
}
