package com.example.zenkart.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zenkart.api.ApiClient
import com.example.zenkart.databinding.ActivityOrderDetailsBinding
import com.example.zenkart.services.Order
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailsBinding
    private lateinit var order: Order // Changed to lateinit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the order JSON string from the intent
        val orderJson = intent.getStringExtra("orderJson")
        Log.d("OrderDetailsActivity", "Received Order JSON: $orderJson") // Log the received JSON

        if (orderJson != null) {
            // Deserialize JSON string to Order object
            order = Gson().fromJson(orderJson, Order::class.java)
            // Populate the views with order details
            binding.orderIdTextView.text = "Order ID: ${order.orderId}"
            binding.orderTotalTextView.text = "Total: $${order.orderTotal}"
            binding.orderStatusTextView.text = "Status: ${order.orderStatus}"

            // Show the cancel button if the order is not yet canceled
            binding.cancelOrderButton.visibility = if (!order.isCancellationRequest) View.VISIBLE else View.GONE
        } else {
            Log.e("OrderDetailsActivity", "Order JSON is null") // Log an error if JSON is null
        }

        // Set click listener for the cancel order button
        binding.cancelOrderButton.setOnClickListener {
            if (!order.isCancellationRequest) {
                cancelOrder(order.orderId)
            }
        }
    }

    private fun cancelOrder(orderId: String) {
        val token = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("jwt_token", null)
        if (token != null) {
            ApiClient.userService.cancelOrder(orderId, "Bearer $token").enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@OrderDetailsActivity, "Order canceled", Toast.LENGTH_SHORT).show()
                        binding.cancelOrderButton.visibility = View.GONE
                        navigateToHomeActivity()
                        finish()
                    } else {
                        Toast.makeText(this@OrderDetailsActivity, "Failed to cancel order", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@OrderDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
