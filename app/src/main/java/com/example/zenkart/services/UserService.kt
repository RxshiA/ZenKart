package com.example.zenkart.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

data class LoginRequest(val Email: String, val PasswordHash: String)
data class RegisterRequest(val name: String, val email: String, val password: String)
data class Product(val id: Int, val name: String, val price: Double, val description: String) {
}

data class CartRequest(val product: Product, val quantity: Int)


interface UserService {
    @POST("api/User/CreateCustomerUser")
    fun registerUser(@Body request: RegisterRequest): Call<Void>

    @POST("api/User/Login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @GET("api/Product/GetAllProducts")
    fun getAllProducts(@Header("Authorization") token: String): Call<List<Product>>

    @GET("api/Product/GetProductById/{id}")
    fun getProductById(@Path("id") productId: String, @Header("Authorization") token: String): Call<Product>

    @POST("api/Cart/AddItemToCart")
    fun addItemToCart(@Body request: CartRequest, @Header("Authorization") token: String): Call<Void>

    @GET("api/Cart/GetUserCart")
    fun getUserCart(@Header("Authorization") token: String): Call<CartResponse>
}
