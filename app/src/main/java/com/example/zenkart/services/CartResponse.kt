package com.example.zenkart.services

data class CartItem(
    val productId: String,
    val productName: String,
    val vendorId: String,
    val quantity: Int,
    val price: Double,
    val name: String
)

data class CartResponse(
    val cartId: String,
    val customerId: String,
    val cartItems: List<CartItem>,
    val cartTotal: Double
)
