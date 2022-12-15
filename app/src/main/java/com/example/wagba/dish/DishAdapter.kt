package com.example.wagba.dish

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wagba.R

class DishAdapter(private val dishList: List<Dish>):
    RecyclerView.Adapter<DishAdapter.DishViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dish, parent, false)
        return DishViewHolder(view)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dish = dishList[position]
        holder.dishImageView.setImageResource(dish.dishImg)
        holder.dishNameTv.text = dish.dishName
        holder.dishPriceTv.text = dish.dishPrice.toString()

    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    class DishViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val dishImageView: ImageView = itemView.findViewById(R.id.dishView)

        val dishNameTv: TextView = itemView.findViewById(R.id.dishName)
        val dishPriceTv: TextView = itemView.findViewById(R.id.dishPrice)


    }
}