package com.example.zenkart.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import com.example.zenkart.api.ApiClient
import com.example.zenkart.adapters.ProductAdapter
import com.example.zenkart.databinding.FragmentHomeBinding
import com.example.zenkart.services.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var productAdapter: ProductAdapter
    private var productList: List<Product> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Set up the GridView and adapter
        productAdapter = ProductAdapter(requireContext(), productList)
        binding.productsGridView.adapter = productAdapter

        // Load products from API
        loadProducts()

        // Set item click listener for GridView
        binding.productsGridView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedProduct = productAdapter.getItem(position)
            Log.d("HomeFragment", "Selected Product: $selectedProduct")
            // Handle product selection (e.g., navigate to ProductDetailsActivity)
        }

        // Set up SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = productList.filter {
                    it.name.contains(newText ?: "", ignoreCase = true)
                }
                productAdapter.updateProductList(filteredList)
                return true
            }
        })

        return binding.root
    }

    private fun loadProducts() {
        val token = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("jwt_token", null)
        if (token != null) {
            ApiClient.userService.getAllProducts("Bearer $token").enqueue(object : Callback<List<Product>> {
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (response.isSuccessful) {
                        productList = response.body() ?: emptyList()
                        productAdapter.updateProductList(productList)
                    } else {
                        Toast.makeText(requireContext(), "Failed to load products", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "Token not found", Toast.LENGTH_SHORT).show()
        }
    }
}
