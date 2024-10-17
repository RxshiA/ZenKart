package com.example.zenkart.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.zenkart.R
import com.example.zenkart.api.ApiClient
import com.example.zenkart.databinding.ActivityProductDetailsBinding
import com.example.zenkart.services.CartRequest
import com.example.zenkart.services.Product
import com.example.zenkart.services.ProductRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productId = intent.getStringExtra("productId")
        val imageID = intent.getStringExtra("imageID")
        if (productId != null) {
            loadProductDetails(productId)
        }
        if (!imageID.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageID)
                .placeholder(R.drawable.ic_product_placeholder)
                .error(R.drawable.ic_product_placeholder)
                .into(binding.productImageView)
        } else {
            binding.productImageView.setImageResource(R.drawable.ic_product_placeholder)
        }

        binding.addToCartButton.setOnClickListener {
            addToCart()
        }
    }

    private fun loadProductDetails(productId: String) {
        val token = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("jwt_token", null)
        if (token != null) {
            ApiClient.userService.getProductById(productId, "Bearer $token").enqueue(object : Callback<Product> {
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    if (response.isSuccessful) {
                        product = response.body()
                        product?.let {
                            binding.productNameTextView.text = it.name
                            binding.productDescriptionTextView.text = it.description
                            binding.productPriceTextView.text = "Price: $${it.price}"
                        }
                    } else {
                        Toast.makeText(this@ProductDetailsActivity, "Failed to load product details", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    Toast.makeText(this@ProductDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun addToCart() {
        val token = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("jwt_token", null)

        if (token != null && product != null) {
            val productRequest = ProductRequest(
                productId = product!!.productId,
                name = product!!.name,
                category = product!!.category,
                description = product!!.description,
                price = product!!.price,
                vendorID = product!!.vendorID,
                quantity = product!!.quantity,
                lowStockAlert = product!!.lowStockAlert,
                isActive = product!!.isActive,
                imageID = product!!.imageID
            )

            val cartRequest = CartRequest(
                product = productRequest,
                quantity = 1 // Default to quantity 1, adjust as needed
            )

            Log.d("ProductDetailsActivity", "Adding to cart: $cartRequest")

            ApiClient.userService.addItemToCart(cartRequest, "Bearer $token").enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ProductDetailsActivity, "Added to cart!", Toast.LENGTH_SHORT).show()
                        navigateToHomeActivity()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("ProductDetailsActivity", "Failed to add to cart: ${response.code()}, Error: $errorBody")
                        Toast.makeText(this@ProductDetailsActivity, "Failed to add to cart: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@ProductDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Product not available or token missing", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
