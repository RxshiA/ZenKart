package com.example.zenkart.activities

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zenkart.api.ApiClient
import com.example.zenkart.databinding.ActivityAddReviewBinding
import com.example.zenkart.services.ReviewRequest
import com.example.zenkart.services.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddReviewBinding
    private lateinit var vendorIds: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadVendorIds()

        binding.submitReviewButton.setOnClickListener {
            val selectedVendorId = binding.vendorSpinner.selectedItem.toString()
            val comment = binding.reviewEditText.text.toString()
            val rating = binding.ratingBar.rating.toInt() // Assuming you have a rating bar for rating input

            if (selectedVendorId.isNotEmpty() && comment.isNotEmpty()) {
                submitReview(selectedVendorId, comment, rating)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadVendorIds() {
        val token = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("jwt_token", null)
        if (token != null) {
            ApiClient.userService.getAllVendors("Bearer $token").enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        val vendors = response.body() ?: emptyList()
                        vendorIds = vendors.map { it.id } // Get vendor IDs
                        val adapter = ArrayAdapter(this@AddReviewActivity, android.R.layout.simple_spinner_item, vendorIds)
                        binding.vendorSpinner.adapter = adapter
                    } else {
                        Toast.makeText(this@AddReviewActivity, "Failed to load vendors", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Toast.makeText(this@AddReviewActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun submitReview(vendorId: String, comment: String, rating: Int) {
        val token = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("jwt_token", null)
        if (token != null) {
            val reviewRequest = ReviewRequest(vendorId, comment, rating)
            ApiClient.userService.createRating(reviewRequest, "Bearer $token").enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddReviewActivity, "Review submitted", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity
                    } else {
                        Toast.makeText(this@AddReviewActivity, "Failed to submit review", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@AddReviewActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
