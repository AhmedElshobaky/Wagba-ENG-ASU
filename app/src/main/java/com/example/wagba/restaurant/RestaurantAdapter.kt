package com.example.wagba.restaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wagba.R

class RestaurantAdapter (private var restaurantList: List<Restaurant>):
    RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.restaurantImageView.setImageResource(restaurant.restaurantImg)
        holder.restaurantNameTv.text = restaurant.restaurantName

    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }


    class RestaurantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val restaurantImageView: ImageView = itemView.findViewById(R.id.restaurantView)

        val restaurantNameTv: TextView = itemView.findViewById(R.id.restaurantName)


    }
    // method for filtering our recyclerview items.
    fun filterList(restaurantFilterList: List<Restaurant>) {
        // below line is to add our filtered
        // list in our course array list.
        restaurantList = restaurantFilterList
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }
}