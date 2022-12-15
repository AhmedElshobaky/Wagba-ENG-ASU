package com.example.wagba.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.wagba.R

class OrderAdapter(private val orderList: List<Order>):
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    lateinit var textView: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.orderImageView.setImageResource(order.orderImg)
        holder.orderDetailsTv.text = order.orderDetails
        holder.orderStatusTv.text = order.orderStatus
        when(order.orderStatus){
            "placed" -> holder.orderStatusTv.background = getDrawable(holder.orderStatusTv.context,R.drawable.placed)
            "on delivery" -> holder.orderStatusTv.background = getDrawable(holder.orderStatusTv.context,R.drawable.ondelivery)
            "delivered" -> holder.orderStatusTv.background = getDrawable(holder.orderStatusTv.context,R.drawable.delivered)
        }



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