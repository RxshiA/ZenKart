package com.example.zenkart.services

data class CartItem(
    val productId: String,
    val vendorId: String,
    val quantity: Int,
    val price: Double
)

data class CartResponse(
    val cartId: String,
    val customerId: String,
    val cartItems: List<CartItem>,
    val cartTotal: Double
)
