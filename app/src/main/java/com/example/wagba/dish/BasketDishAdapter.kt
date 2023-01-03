package com.example.wagba.dish

import android.hardware.ConsumerIrManager.CarrierFrequencyRange
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wagba.R

class BasketDishAdapter (private val dishList: List<Dish>, private val dishFrequency: List<Int>):
    RecyclerView.Adapter<BasketDishAdapter.BasketDishViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketDishAdapter.BasketDishViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_basket, parent, false)
        return BasketDishAdapter.BasketDishViewHolder(view)
    }

    override fun onBindViewHolder(holder: BasketDishViewHolder, position: Int) {

        val dish = dishList[position]
        val dishPrice = "${dish.Price * dishFrequency[position]} EGP"
        val dishQuantity = "${dishFrequency[position]}"
        holder.basketNameTv.text = dish.Name
        holder.basketPriceTv.text = dishPrice
        holder.basketQuantityTv.text = dishQuantity
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    class BasketDishViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val basketNameTv: TextView = itemView.findViewById(R.id.column_1)
        val basketQuantityTv: TextView = itemView.findViewById(R.id.column_2)
        val basketPriceTv: TextView = itemView.findViewById(R.id.column_3)


    }


}