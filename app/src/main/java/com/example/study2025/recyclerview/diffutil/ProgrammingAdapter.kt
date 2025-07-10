package com.example.study2025.recyclerview.diffutil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.study2025.R

class MainActivity18 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rv)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = ProgrammingAdapter()

        val p1 = ProgItem(1, "G", "Google")
        val p2 = ProgItem(2, "M", "Meta")
        val p3 = ProgItem(3, "T", "Tesla")

        adapter.submitList(listOf(p1,p2,p3))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ProgrammingAdapter()
    }
}

data class ProgItem(val id: Int, val initial: String, val name: String)

class ProgrammingAdapter : ListAdapter<ProgItem, ProgrammingAdapter.ProgrammingViewHolder>(DIFF_CALLBACK) {

    class ProgrammingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.textName)

        fun bind(item: ProgItem) {
            name.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgrammingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false)
        return ProgrammingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProgrammingViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<ProgItem>(){
            override fun areItemsTheSame(oldItem: ProgItem, newItem: ProgItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProgItem, newItem: ProgItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
