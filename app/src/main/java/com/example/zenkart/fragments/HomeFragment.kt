package com.example.zenkart.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.zenkart.activities.ProductDetailsActivity
import com.example.zenkart.adapters.ProductAdapter
import com.example.zenkart.api.ApiClient
import com.example.zenkart.databinding.FragmentHomeBinding
import com.example.zenkart.services.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Load products
        loadProducts()

        // Set item click listener for ListView
        binding.productsListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedProduct = binding.productsListView.adapter.getItem(position) as Product
            Log.d("HomeFragment", "Selected Product: $selectedProduct")
            val intent = Intent(requireContext(), ProductDetailsActivity::class.java)
            intent.putExtra("productId", selectedProduct.productId) // Ensure 'id' matches your Product data class
            startActivity(intent)
        }

        return binding.root
    }

    private fun loadProducts() {
        val token = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("jwt_token", null)
        if (token != null) {
            ApiClient.userService.getAllProducts("Bearer $token").enqueue(object : Callback<List<Product>> {
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (response.isSuccessful) {
                        val products = response.body() ?: emptyList()
                        // Ensure ProductAdapter is set up correctly
                        val adapter = ProductAdapter(requireContext(), products)
                        binding.productsListView.adapter = adapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}
