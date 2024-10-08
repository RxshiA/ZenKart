package com.example.zenkart.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.zenkart.activities.OrderListActivity
import com.example.zenkart.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Track Orders Click Listener
        binding.trackOrders.setOnClickListener {
            val userId = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("user_id", null)
            val intent = Intent(requireContext(), OrderListActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        // Rate Orders Click Listener
        binding.rateOrders.setOnClickListener {
            // Implement rate orders functionality
        }

        // Deactivate Account Click Listener
        binding.deactivateAccountButton.setOnClickListener {
            // Implement deactivate account functionality
        }

        return binding.root
    }
}
