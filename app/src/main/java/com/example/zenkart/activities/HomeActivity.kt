package com.example.zenkart.activities

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zenkart.R
import com.example.zenkart.adapters.ProductAdapter
import com.example.zenkart.api.ApiClient
import com.example.zenkart.databinding.ActivityHomeBinding
import com.example.zenkart.services.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    // ViewBinding instance for this activity
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up Bottom Navigation listener using the new method
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Already in home, do nothing
                    true
                }
                R.id.navigation_cart -> {
                    // Open CartActivity
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    // Open ProfileActivity (you can uncomment and add your ProfileActivity later)
                    // startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
        binding.productsListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // Get the selected product
            val selectedProduct = binding.productsListView.adapter.getItem(position) as Product

            // Start ProductDetailsActivity and pass the product ID
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra("productId", selectedProduct.id)
            startActivity(intent)
        }

        // Load products into the list (already implemented)
        loadProducts()
    }

    private fun loadProducts(){
        val token = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("jwt_token", "")
        ApiClient.userService.getAllProducts("Bearer $token").enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val products = response.body() ?: emptyList()
                    // Set up the adapter for the ListView
                    val adapter = ProductAdapter(this@HomeActivity, products)
                    binding.productsListView.adapter = adapter
                } else {
                    Toast.makeText(this@HomeActivity, "Failed to load products", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
