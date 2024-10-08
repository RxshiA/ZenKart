package com.example.zenkart.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zenkart.adapters.RatingAdapter
import com.example.zenkart.api.ApiClient
import com.example.zenkart.databinding.ActivityVendorRatingsBinding
import com.example.zenkart.services.VendorRating
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendorRatingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVendorRatingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendorRatingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadVendorRatings()

        binding.addReviewButton.setOnClickListener {
            startActivity(Intent(this, AddReviewActivity::class.java))
        }
    }

    private fun loadVendorRatings() {
        val token = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("jwt_token", null)
        if (token != null) {
            ApiClient.userService.getVendorRatings("Bearer $token").enqueue(object : Callback<List<VendorRating>> {
                override fun onResponse(call: Call<List<VendorRating>>, response: Response<List<VendorRating>>) {
                    if (response.isSuccessful) {
                        val ratings = response.body() ?: emptyList()
                        val adapter = RatingAdapter(this@VendorRatingsActivity, ratings)
                        binding.ratingsListView.adapter = adapter
                    } else {
                        Toast.makeText(this@VendorRatingsActivity, "Failed to load ratings", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<VendorRating>>, t: Throwable) {
                    Toast.makeText(this@VendorRatingsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
