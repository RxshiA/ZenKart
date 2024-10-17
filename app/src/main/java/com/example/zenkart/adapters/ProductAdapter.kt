package com.example.zenkart.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.zenkart.R
import com.example.zenkart.services.Product
import com.bumptech.glide.Glide
import com.example.zenkart.activities.ProductDetailsActivity

class ProductAdapter(private val context: Context, private var productList: List<Product>) : BaseAdapter() {

    override fun getCount(): Int = productList.size

    override fun getItem(position: Int): Product = productList[position]

    override fun getItemId(position: Int): Long = productList[position].id.toLong()

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)

        // Get the current product
        val product = getItem(position)

        // Set product image, name, and price
        val productNameTextView = view.findViewById<TextView>(R.id.productNameTextView)
        val productPriceTextView = view.findViewById<TextView>(R.id.productPriceTextView)
        val productImageView = view.findViewById<ImageView>(R.id.productImageView)

        productNameTextView.text = product.name
        productPriceTextView.text = "Price: $${product.price}"

        // Load product image using Glide or any other image loading library
        val imageUrl = product.imageID  // Assuming the image URL is stored in the 'imageID' field of the Product class
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_product_placeholder) // Show placeholder while loading
            .error(R.drawable.ic_product_placeholder) // Show placeholder if there's an error
            .into(productImageView)

        // Set click listener to navigate to ProductDetailsActivity
        view.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("productId", product.productId)
            intent.putExtra("imageID", product.imageID)
            context.startActivity(intent)
        }

        return view
    }

    // Method to update the product list and notify changes
    @SuppressLint("NotifyDataSetChanged")
    fun updateProductList(newProductList: List<Product>) {
        productList = newProductList
        notifyDataSetChanged()
    }
}
