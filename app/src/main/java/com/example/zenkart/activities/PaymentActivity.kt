package com.example.zenkart.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zenkart.api.ApiClient
import com.example.zenkart.databinding.ActivityPaymentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle "Pay Now" button click
        binding.payNowButton.setOnClickListener {
            checkoutCart()
        }
    }

    private fun checkoutCart() {
        val token = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("jwt_token", null)
        val userId = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("user_id", null)
        Log.d("PaymentActivity", "User ID: $userId")
        if (token != null && userId != null) {
            ApiClient.userService.checkoutCart(userId, "Bearer $token").enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@PaymentActivity, "Payment successful", Toast.LENGTH_SHORT).show()
                        navigateToHomeActivity()
                        finish()
                    } else {
                        val errorMessage = when (response.code()) {
                            400 -> "Bad Request"
                            else -> "Payment failed: ${response.message()}"
                        }
                        Log.e("PaymentActivity", "Checkout failed: $errorMessage")
                        Toast.makeText(this@PaymentActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@PaymentActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // Navigate to HomeActivity after login
    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
