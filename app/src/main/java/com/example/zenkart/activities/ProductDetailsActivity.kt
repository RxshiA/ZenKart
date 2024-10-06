package com.example.zenkart.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zenkart.api.ApiClient
import com.example.zenkart.databinding.ActivityProductDetailsBinding
import com.example.zenkart.services.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productId = intent.getStringExtra("productId")

        if (productId != null) {
            loadProductDetails(productId)
        }

        binding.addToCartButton.setOnClickListener {
            addToCart(productId)
        }
    }

    private fun loadProductDetails(productId: String) {
        val token = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("jwt_token", null)
        if (token != null) {
            ApiClient.userService.getProductById(productId, "Bearer $token").enqueue(object : Callback<Product> {
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    if (response.isSuccessful) {
                        val product = response.body()
                        if (product != null) {
                            // Display product details
                            binding.productNameTextView.text = product.name
                            binding.productDescriptionTextView.text = product.description
                            binding.productPriceTextView.text = "Price: $${product.price}"
                        }
                    }
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    Toast.makeText(this@ProductDetailsActivity, "Failed to load product details", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun addToCart(productId: String?) {
        // Code to add product to cart
    }
}
