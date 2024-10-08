package com.example.zenkart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
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

        productNameTextView.text = item.name
        productPriceTextView.text = "Price: $${item.price}"

        return view
    }
}
