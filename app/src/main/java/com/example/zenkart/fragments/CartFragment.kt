package com.example.zenkart.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.zenkart.api.ApiClient
import com.example.zenkart.databinding.FragmentCartBinding
import com.example.zenkart.services.CartResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        // Load cart items when the fragment view is created
        loadCartItems()

        return binding.root
    }

    private fun loadCartItems() {
        val token = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("jwt_token", null)
        if (token != null) {
            ApiClient.userService.getUserCart("Bearer $token").enqueue(object : Callback<CartResponse> {
                override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>) {
                    if (response.isSuccessful) {
                        val cart = response.body()
                        // Display cart items and total
                        // You can now use binding to update the UI
                        binding.cartTotalTextView.text = "Total: $${cart?.cartTotal ?: 0}"
                    } else {
                        Toast.makeText(requireContext(), "Failed to load cart items", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
