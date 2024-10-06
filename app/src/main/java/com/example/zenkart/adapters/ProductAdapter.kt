package com.example.zenkart.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.zenkart.R
import com.example.zenkart.services.Product

data class Product(val id: Int, val name: String, val price: Double)

class ProductAdapter(private val context: Context, private val productList: List<com.example.zenkart.services.Product>) : BaseAdapter() {

    override fun getCount(): Int = productList.size

    override fun getItem(position: Int): Product = productList[position]

    override fun getItemId(position: Int): Long = productList[position].id.toLong()

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)

        // Get the current product
        val product = getItem(position)

        // Set product name and price
        val productNameTextView = view.findViewById<TextView>(R.id.productNameTextView)
        val productPriceTextView = view.findViewById<TextView>(R.id.productPriceTextView)

        productNameTextView.text = product.name
        productPriceTextView.text = "Price: $${product.price}"

        return view
    }
}
