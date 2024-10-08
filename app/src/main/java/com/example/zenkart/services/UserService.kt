package com.example.zenkart.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

data class User(val id: String, val email: String, val passwordHash: String, val role: String, val isActive: String, val isApproved: String, val vendorReviews: List<Review>)
data class LoginRequest(val Email: String, val PasswordHash: String)
data class RegisterRequest(val name: String, val email: String, val passwordHash: String)
data class Product(val id: Int, val productId: String, val name: String, val category: String, val vendorID: String, val quantity: Int, val lowStockAlert: Int, val isActive: Boolean, val price: Double, val description: String)
data class ProductRequest(
    val productId: String,
    val name: String,
    val category: String,
    val description: String,
    val price: Double,
    val vendorID: String,
    val quantity: Int,
    val lowStockAlert: Int,
    val isActive: Boolean
)
data class CartRequest(
    val product: ProductRequest,
    val quantity: Int
)
data class OrderItem(
    val productId: String,
    val vendorId: String,
    val quantity: Int,
    val price: Double,
    val delivaryStatus: String
)
data class Order(
    val orderId: String,
    val customerId: String,
    val orderStatus: String,
    val orderItems: List<OrderItem>,
    val orderTotal: Double,
    val createdDate: String,
    val isCancellationRequest: Boolean
)
data class ReviewRequest(val vendorID: String, val comment: String, val rating: Int)
data class VendorRating(val id: String, val email: String, val vendorReviews: List<Review>)
data class Review(val customerID: String?, val rating: Int, val comment: String, val createdDate: String)

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

    @POST("api/Cart/CheckOutCart")
    fun checkoutCart(@Query("userId") userId: String, @Header("Authorization") token: String): Call<Void>

    @GET("api/Order/ViewCustomerOrders/{userId}")
    fun getCustomerOrders(@Path("userId") userId: String, @Header("Authorization") token: String): Call<List<Order>>

    @PATCH("api/Order/OrderCancelRequest/{orderId}")
    fun cancelOrder(@Path("orderId") orderId: String, @Header("Authorization") token: String): Call<Void>

    @GET("api/VendorRating/CustomersVendorRatings")
    fun getVendorRatings(@Header("Authorization") token: String): Call<List<VendorRating>>

    @GET("api/User/GetAllUsers")
    fun getAllVendors(@Header("Authorization") token: String): Call<List<User>>

    @POST("api/VendorRating/CreateRating")
    fun createRating(@Body request: ReviewRequest, @Header("Authorization") token: String): Call<Void>
}
