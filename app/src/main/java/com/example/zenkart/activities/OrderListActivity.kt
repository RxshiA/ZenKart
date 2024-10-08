package com.example.zenkart.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zenkart.adapters.OrderAdapter
import com.example.zenkart.api.ApiClient
import com.example.zenkart.databinding.ActivityOrderListBinding
import com.example.zenkart.services.Order
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra("userId")
        if (userId != null) {
            loadOrders(userId)
        }

        // Set up the ListView item click listener
        binding.ordersListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedOrder = parent.getItemAtPosition(position) as Order
            Log.d("OrderListActivity", "Selected Order: $selectedOrder") // Log the selected order
            // Pass the selected order to OrderDetailsActivity
            val intent = Intent(this, OrderDetailsActivity::class.java)
            val orderJson = Gson().toJson(selectedOrder) // Serialize order to JSON
            intent.putExtra("orderJson", orderJson) // Pass the JSON string
            startActivity(intent)
        }
    }

    private fun loadOrders(userId: String) {
        val token = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("jwt_token", null)
        if (token != null) {
            ApiClient.userService.getCustomerOrders(userId, "Bearer $token").enqueue(object : Callback<List<Order>> {
                override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                    if (response.isSuccessful) {
                        val orders = response.body() ?: emptyList()
                        val adapter = OrderAdapter(this@OrderListActivity, orders)
                        binding.ordersListView.adapter = adapter
                    } else {
                        Toast.makeText(this@OrderListActivity, "Failed to load orders", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                    Toast.makeText(this@OrderListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
