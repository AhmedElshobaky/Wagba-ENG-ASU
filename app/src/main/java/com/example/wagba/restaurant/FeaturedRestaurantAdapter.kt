package com.example.wagba.restaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wagba.R
import com.example.wagba.RestaurantDishes

class FeaturedRestaurantAdapter(private val restaurantList: List<Restaurant>):
    RecyclerView.Adapter<FeaturedRestaurantAdapter.FeaturedRestaurantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedRestaurantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_featured_restaurant, parent, false)
        return FeaturedRestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeaturedRestaurantViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        Glide.with(holder.restaurantImageView.context)
            .load(restaurant.featuredImg)
            .into(holder.restaurantImageView)
        holder.restaurantNameTv.text = restaurant.Name

        holder.itemView.setOnClickListener {
            val fragment = RestaurantDishes.newInstance(restaurant)
            val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
        }

    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    class FeaturedRestaurantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val restaurantImageView: ImageView = itemView.findViewById(R.id.featuredRestaurantView)

        val restaurantNameTv: TextView = itemView.findViewById(R.id.featuredRestaurantName)


    }
}