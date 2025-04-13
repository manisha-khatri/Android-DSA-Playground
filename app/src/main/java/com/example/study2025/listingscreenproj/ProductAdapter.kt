package com.example.study2025.listingscreenproj

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.study2025.databinding.ItemProductBinding
import com.example.study2025.listingscreenproj.local.Product

class ProductAdapter(private val onItemClick: (Product) -> Unit) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val items = mutableListOf<Product>()

    fun submitList(newList: List<Product>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = items[position]
        holder.binding.textName.text = product.name
        holder.itemView.setOnClickListener {
            onItemClick(product)
        }
    }

    override fun getItemCount(): Int = items.size
}
