package com.example.wagba.order
import com.example.wagba.dish.Dish
import java.util.*

data class Order(val orderId: String, val restaurantId: String, val userId:String, val dishes: List<Dish>, val totalAmount: Double, val gate: String, val time: String, var orderStatus: String, val currentTime: Date, val logo: String) {
    constructor() : this("", "", "", emptyList(), 0.0, "", "", "",Date(), "")
}