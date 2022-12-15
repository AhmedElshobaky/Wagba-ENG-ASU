package com.example.wagba.food

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wagba.R

class FoodAdapter(private val foodList: List<Food>):
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foodList[position]
        holder.foodImageView.setImageResource(food.foodImg)
        holder.foodNameTv.text = food.foodName

    }

    override fun getItemCount(): Int {
        return foodList.size
    }
    class FoodViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val foodImageView: ImageView = itemView.findViewById(R.id.foodView)

        val foodNameTv: TextView = itemView.findViewById(R.id.foodName)


    }
}