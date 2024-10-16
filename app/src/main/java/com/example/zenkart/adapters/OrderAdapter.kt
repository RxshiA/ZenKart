package com.example.zenkart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.zenkart.R
import com.example.zenkart.services.Order

class OrderAdapter(private val context: Context, private val orders: List<Order>) : BaseAdapter() {

    override fun getCount(): Int = orders.size

    override fun getItem(position: Int): Order = orders[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)

        // Assuming you have TextViews for order details
        val order = getItem(position)
        val orderIdTextView = view.findViewById<TextView>(R.id.orderIdTextView)
        orderIdTextView.text = order.orderId // Set the order ID or any other details
        val orderTotalTextView = view.findViewById<TextView>(R.id.orderTotalTextView)
        orderTotalTextView.text = order.orderTotal.toString() // Set the order total
        val orderStatusTextView = view.findViewById<TextView>(R.id.orderStatusTextView)
        orderStatusTextView.text = order.orderStatus // Set the order status

        return view
    }
}
