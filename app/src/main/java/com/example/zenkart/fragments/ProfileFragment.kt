package com.example.zenkart.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.zenkart.activities.LoginActivity
import com.example.zenkart.activities.OrderListActivity
import com.example.zenkart.activities.VendorRatingsActivity
import com.example.zenkart.api.ApiClient
import com.example.zenkart.databinding.FragmentProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            startActivity(Intent(requireContext(), VendorRatingsActivity::class.java))
        }

        // Deactivate Account Click Listener
        binding.deactivateAccountButton.setOnClickListener {
            showDeactivateAccountDialog()
        }

        return binding.root
    }

    // Show a confirmation dialog before deactivation
    private fun showDeactivateAccountDialog() {
        val userId = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("user_id", null)

        // Show confirmation dialog
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Deactivate Account")
            .setMessage("Are you sure that you want to deactivate this account?")
            .setPositiveButton("Yes") { dialogInterface, _ ->
                if (userId != null) {
                    deactivateAccount(userId)
                }
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        alertDialog.show()
    }

    // API call to deactivate the account
    private fun deactivateAccount(userId: String) {
        val token = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("jwt_token", null)

        if (token != null) {
            ApiClient.userService.deactivateCustomerAccount(userId, "Bearer $token").enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // Show success toast
                        Toast.makeText(requireContext(), "Account deactivated successfully", Toast.LENGTH_SHORT).show()

                        // Redirect to LoginActivity
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        Toast.makeText(requireContext(), "Failed to deactivate account", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "Token not found", Toast.LENGTH_SHORT).show()
        }
    }
}
