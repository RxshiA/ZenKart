package com.example.zenkart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.zenkart.R
import com.example.zenkart.services.CartItem

class CartAdapter(private val context: Context, private val cartItems: List<CartItem>) : BaseAdapter() {

    override fun getCount(): Int = cartItems.size

    override fun getItem(position: Int): CartItem = cartItems[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)

        val item = getItem(position)

        // Set cart item details
        val productNameTextView = view.findViewById<TextView>(R.id.productNameTextView)
        val productPriceTextView = view.findViewById<TextView>(R.id.productPriceTextView)
        val productQuantityTextView = view.findViewById<TextView>(R.id.productQuantityTextView)
        val productImageView = view.findViewById<ImageView>(R.id.productImageView)

        val imageUrl = item.productImage
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_product_placeholder)
            .error(R.drawable.ic_product_placeholder)
            .into(productImageView)

        productNameTextView.text = item.productName
        productPriceTextView.text = "Price: $${item.price}"
        productQuantityTextView.text = "Quantity: ${item.quantity}"

        return view
    }
}
