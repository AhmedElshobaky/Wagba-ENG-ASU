package com.example.wagba.order

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wagba.R
import java.text.SimpleDateFormat

class OrderAdapter(private var orderList: List<Order>):
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {

        val order = orderList[position]
        Log.d("logoBug", "onBindViewHolder: ORDER.LOGO ${order.logo}")   //returns: link.jpg to the logo
        holder.orderDetailsTv.text = getOrderDetails(order)
        holder.orderStatusTv.text = order.orderStatus
        //_______________________________________________________
        //holder.orderImageView =
       //Picasso.get().load(order.logo).into(holder.orderImageView)
        //_______________________________________________________
        Glide.with(holder.orderImageView.context)
            .load(order.logo)
            .into(holder.orderImageView)

        when(order.orderStatus){
            "placed" -> holder.orderStatusTv.background = getDrawable(holder.orderStatusTv.context,R.drawable.placed)
            "on delivery" -> holder.orderStatusTv.background = getDrawable(holder.orderStatusTv.context,R.drawable.ondelivery)
            "delivered" -> holder.orderStatusTv.background = getDrawable(holder.orderStatusTv.context,R.drawable.delivered)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getOrderDetails(order: Order): CharSequence {
        val currentTime = SimpleDateFormat("HH:mm dd/MM").format(order.currentTime)
        return "Date: "+currentTime+"\nOrder Id: " +  order.orderId.takeLast(6)  + "\n"  + "Total: " + order.totalAmount + " EGP" + "\n" +order.gate + " - " + order.time
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
    class OrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val orderImageView: ImageView = itemView.findViewById(R.id.orderView)
        val orderDetailsTv: TextView = itemView.findViewById(R.id.orderDetails)
        val orderStatusTv: TextView = itemView.findViewById(R.id.orderStatus)


    }
}