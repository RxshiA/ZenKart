package com.example.zenkart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.zenkart.R
import com.example.zenkart.services.VendorRating

class RatingAdapter(private val context: Context, private val ratings: List<VendorRating>) : BaseAdapter() {

    override fun getCount(): Int {
        return ratings.size
    }

    override fun getItem(position: Int): VendorRating {
        return ratings[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_rating, parent, false)

        val vendorRating = getItem(position)

        val vendorNameTextView: TextView = view.findViewById(R.id.vendorNameTextView)
        val ratingTextView: TextView = view.findViewById(R.id.ratingTextView)
        val commentTextView: TextView = view.findViewById(R.id.commentTextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)

        // Assuming the VendorRating contains a list of reviews
        vendorRating.vendorReviews.forEach { review ->
            vendorNameTextView.text = vendorRating.email // Display the vendor's email
            ratingTextView.text = "Rating: ${review.rating}"
            commentTextView.text = review.comment
            dateTextView.text = review.createdDate // Format this date as necessary
        }

        return view
    }
}
