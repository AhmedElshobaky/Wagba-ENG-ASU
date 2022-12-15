package com.example.wagba

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.wagba.dish.Dish
import com.example.wagba.dish.DishAdapter


import com.example.wagba.restaurant.Restaurant
import com.example.wagba.restaurant.FeaturedRestaurantAdapter
import com.example.wagba.restaurant.RestaurantAdapter

import com.example.wagba.food.Food
import com.example.wagba.food.FoodAdapter
import java.util.*
import kotlin.collections.ArrayList






/**
 * A simple [Fragment] subclass.
 * Use the [Delivery.newInstance] factory method to
 * create an instance of this fragment.
 */

class Delivery : Fragment() , AdapterView.OnItemSelectedListener {

    private lateinit var featuredrestaurantRV: RecyclerView
    private lateinit var featuredRestaurantList:ArrayList<Restaurant>
    private lateinit var featuredrestaurantAdapter: FeaturedRestaurantAdapter

    private lateinit var restaurantRV: RecyclerView
    private lateinit var restaurantList:ArrayList<Restaurant>
    private lateinit var restaurantAdapter: RestaurantAdapter

    private lateinit var foodRV: RecyclerView
    private lateinit var foodList:ArrayList<Food>
    private lateinit var foodAdapter: FoodAdapter

    private lateinit var searchV: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delivery, container, false)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchV = view.findViewById(R.id.searchView)
        foodRV = view.findViewById(R.id.foodRecyclerView)
        featuredrestaurantRV = view.findViewById(R.id.featuredRestaurantRecyclerView)
        restaurantRV = view.findViewById(R.id.restaurantRecyclerView)




        searchV_init()
        foodRV_init()
        featuredrestaurantRV_init()
        restaurantRV_init()

    }

    private fun searchV_init(){
        searchV.clearFocus()
        searchV.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String): Boolean {
                filter(p0)
                return false
            }

        })
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<Restaurant> = ArrayList()

        // running a for loop to compare elements.
        for (item in restaurantList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.restaurantName.lowercase().contains(text.lowercase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(requireActivity(), "No Restaurants Found..", Toast.LENGTH_SHORT).show()

        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            restaurantAdapter.filterList(filteredlist)
        }
    }

    private fun restaurantRV_init() {
        restaurantRV.setHasFixedSize(true)
        restaurantRV.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        val snapHelper:SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(restaurantRV)

        restaurantList = ArrayList()
        addRestaurantToList()

        restaurantAdapter = RestaurantAdapter(restaurantList)
        restaurantRV.adapter = restaurantAdapter
    }


    private fun featuredrestaurantRV_init(){
        featuredrestaurantRV.setHasFixedSize(true)
        featuredrestaurantRV.layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
        val snapHelper:SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(featuredrestaurantRV)

        featuredRestaurantList = ArrayList()
        addFeaturedRestaurantToList()

        featuredrestaurantAdapter = FeaturedRestaurantAdapter(featuredRestaurantList)
        featuredrestaurantRV.adapter = featuredrestaurantAdapter
    }
    private fun addFeaturedRestaurantToList(){
        featuredRestaurantList.add(Restaurant(R.drawable.mcdonalds_offer,this.resources.getString(R.string.mcdonalds)))
        featuredRestaurantList.add(Restaurant(R.drawable.papajohns_offer,this.resources.getString(R.string.papajohns)))
        featuredRestaurantList.add(Restaurant(R.drawable.starbucks_offer,this.resources.getString(R.string.starbucks)))
        featuredRestaurantList.add(Restaurant(R.drawable.sadam_offer,this.resources.getString(R.string.sadam)))
    }
    private fun addRestaurantToList(){
        restaurantList.add(Restaurant(R.drawable.mcdonalds_logo_504,this.resources.getString(R.string.mcdonalds)))
        restaurantList.add(Restaurant(R.drawable.papajohns_logo_504,this.resources.getString(R.string.papajohns)))
        restaurantList.add(Restaurant(R.drawable.starbucks_logo_504,this.resources.getString(R.string.starbucks)))
        restaurantList.add(Restaurant(R.drawable.sadam_logo_504,this.resources.getString(R.string.sadam)))
        restaurantList.add(Restaurant(R.drawable.cilantro_logo_504,this.resources.getString(R.string.cilantro)))
        restaurantList.add(Restaurant(R.drawable.dd_logo_504,this.resources.getString(R.string.dunkindonuts)))
        restaurantList.add(Restaurant(R.drawable.kfc_logo_504,this.resources.getString(R.string.kfc)))
        restaurantList.add(Restaurant(R.drawable.kosharyeltahrir_logo_504,this.resources.getString(R.string.kosharyeltahrir)))
        restaurantList.add(Restaurant(R.drawable.zainelsham_logo_504,this.resources.getString(R.string.zainelsham)))

    }


    private fun foodRV_init(){
        foodRV.setHasFixedSize(true)
        foodRV.layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
        val snapHelper:SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(foodRV)

        foodList = ArrayList()
        addFoodToList()

        foodAdapter = FoodAdapter(foodList)
        foodRV.adapter = foodAdapter
    }
    private fun addFoodToList(){
        foodList.add(Food(R.drawable.sandwich,this.resources.getString(R.string.sandwich)))
        foodList.add(Food(R.drawable.shawarma,this.resources.getString(R.string.shawarma)))
        foodList.add(Food(R.drawable.noodles,this.resources.getString(R.string.noodles)))
        foodList.add(Food(R.drawable.coffee,this.resources.getString(R.string.coffee)))
        foodList.add(Food(R.drawable.bakery,this.resources.getString(R.string.bakery)))
        foodList.add(Food(R.drawable.pizza,this.resources.getString(R.string.pizza)))
        foodList.add(Food(R.drawable.hamburger,this.resources.getString(R.string.hamburger)))



    }

}