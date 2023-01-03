package com.example.wagba.dish

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wagba.R



class DishAdapter(private val dishList: List<Dish>):
    RecyclerView.Adapter<DishAdapter.DishViewHolder>(){

    private val basketDishes = mutableListOf<Dish>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dish, parent, false)



        return DishViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dish = dishList[position]
        Glide.with(holder.dishImageView.context)
            .load(dish.Img)
            .into(holder.dishImageView)
        holder.dishNameTv.text = dish.Name
        holder.dishPriceTv.text = "${dish.Price} EGP"

        if (dish.available){
            // if addic clicked it will be added to basketDishes
            holder.dishAddIC.setOnClickListener {
                basketDishes.add(dish)
                Toast.makeText(holder.dishAddIC.context, dish.Name + " added to basket", Toast.LENGTH_SHORT).show()
            }
        }else{
            holder.dishAddIC.isInvisible = true
        }
    }

    override fun getItemCount(): Int {

        return dishList.size
    }
    public fun getBasketDishes(): List<Dish>{
        return basketDishes
    }


    class DishViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val dishImageView: ImageView = itemView.findViewById(R.id.dishView)

        val dishNameTv: TextView = itemView.findViewById(R.id.dishName)
        val dishPriceTv: TextView = itemView.findViewById(R.id.dishPrice)
        val dishAddIC: ImageView = itemView.findViewById(R.id.addItem)


    }
}