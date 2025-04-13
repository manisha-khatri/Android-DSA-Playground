package com.example.study2025.listingscreenproj

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.study2025.databinding.ActivityProductListBinding
import com.example.study2025.listingscreenproj.local.Product

class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var viewModel: ProductViewModel
    private val adapter = ProductAdapter { product ->
        Toast.makeText(this, "Clicked: ${product.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupViewModel()
        observeUiState()
        setupFilterButtons()

        viewModel.seedData() // Seed and load all products
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
    }

    private fun observeUiState() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> showLoading()
                    is UiState.Empty -> showEmpty()
                    is UiState.Success -> showProducts(state.products)
                    is UiState.Error -> showError(state.message)
                }
            }
        }
    }

    private fun setupFilterButtons() {
        binding.buttonAll.setOnClickListener {
            viewModel.loadAllProducts() // Cached on first call
        }
        binding.buttonFruits.setOnClickListener {
            viewModel.filterByCategory("Fruits")
        }
        binding.buttonVegetables.setOnClickListener {
            viewModel.filterByCategory("Vegetables")
        }
    }

    private fun showLoading() {
        binding.recyclerView.visibility = View.GONE
        binding.textViewMessage.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.textViewMessage.apply {
            visibility = View.VISIBLE
            text = "No products found."
        }
    }

    private fun showError(message: String) {
        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.textViewMessage.apply {
            visibility = View.VISIBLE
            text = message
        }
    }

    private fun showProducts(products: List<Product>) {
        binding.progressBar.visibility = View.GONE
        binding.textViewMessage.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        adapter.submitList(products)
    }
}


