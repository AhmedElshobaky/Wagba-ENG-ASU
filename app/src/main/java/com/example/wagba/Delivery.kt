package com.example.wagba

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.wagba.dish.Dish


import com.example.wagba.restaurant.Restaurant
import com.example.wagba.restaurant.FeaturedRestaurantAdapter
import com.example.wagba.restaurant.RestaurantAdapter

import com.example.wagba.food.Food
import com.example.wagba.food.FoodAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList






/**
 * A simple [Fragment] subclass.
 * Use the [Delivery.newInstance] factory method to
 * create an instance of this fragment.
 */

class Delivery : Fragment() , AdapterView.OnItemSelectedListener {


    private lateinit var featuredrestaurantRV: RecyclerView
    private var featuredRestaurantsList = mutableListOf<Restaurant>()
    private lateinit var featuredrestaurantAdapter: FeaturedRestaurantAdapter

    private lateinit var restaurantRV: RecyclerView
    private lateinit var restaurantAdapter:RestaurantAdapter

    private lateinit var foodRV: RecyclerView
    private lateinit var foodList:ArrayList<Food>
    private lateinit var foodAdapter: FoodAdapter

    private lateinit var searchV: SearchView
    private lateinit var welcomeUserTv: TextView

    private var restaurantsList = mutableListOf<Restaurant>()


    private val database = FirebaseDatabase.getInstance("https://wagba-db-default-rtdb.firebaseio.com/")
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val restaurantRef = database.getReference("Restaurant")
    private var msg = ""


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
        welcomeUserTv = view.findViewById(R.id.welcomeUser)
        // potential filtering using categories
        //foodRV = view.findViewById(R.id.foodRecyclerView)
        featuredrestaurantRV = view.findViewById(R.id.featuredRestaurantRecyclerView)
        restaurantRV = view.findViewById(R.id.restaurantRecyclerView)




        setGreetingMsg()
        searchV_init()
        //foodRV_init()
        featuredrestaurantRV_init()
        restaurantRV_init()

    }

    private fun setGreetingMsg() {

        val calendar = Calendar.getInstance()
        val timeOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val partMsg =  when (timeOfDay) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            in 16..20 -> "Good Evening"
            in 21..23 -> "Hello"
            else -> "Hello"
        }

        val userId = firebaseAuth.currentUser!!.uid
        val userRef = database.getReference("users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("username").value.toString()
                msg = "$partMsg, $username!"
                welcomeUserTv.text = msg
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
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
        for (item in restaurantsList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.Name.lowercase().contains(text.lowercase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(requireActivity(), "No Restaurants Found..", Toast.LENGTH_SHORT).show()

        } else {
           restaurantAdapter.filterList(filteredlist)
        }
    }

    private fun restaurantRV_init() {
        restaurantRV.setHasFixedSize(true)
        restaurantRV.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        val snapHelper:SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(restaurantRV)

        addRestaurantFromDB()
    }


    private fun featuredrestaurantRV_init(){
        featuredrestaurantRV.setHasFixedSize(true)
        featuredrestaurantRV.layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
        val snapHelper:SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(featuredrestaurantRV)
        addFeaturedRestaurantFromDB()
    }

    private fun addFeaturedRestaurantFromDB(){
        restaurantRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val featuredRestaurants = dataSnapshot.children.mapNotNull { snapshot ->
                    val featuredRestaurant = snapshot.getValue(Restaurant::class.java)
                    if (featuredRestaurant != null && featuredRestaurant.featured) {
                            featuredRestaurant.restaurantId = snapshot.key.toString()
                        featuredRestaurant
                        }else{
                            null
                        }
                }
                Log.d("featuredRestaurant", featuredRestaurants.toString())
                featuredRestaurantsList.addAll(featuredRestaurants)
                // set adapter after retrieving the restaurants from the database
                featuredrestaurantAdapter = FeaturedRestaurantAdapter(featuredRestaurantsList)
                featuredrestaurantRV.adapter = featuredrestaurantAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }



    private fun addRestaurantFromDB() {
        restaurantRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val restaurants = dataSnapshot.children.mapNotNull { snapshot ->
                    val restaurant = snapshot.getValue(Restaurant::class.java)
                    if (restaurant != null) {
                        restaurant.restaurantId = snapshot.key.toString()
                    }
                    restaurant
                }
                restaurantsList.addAll(restaurants)
                // set adapter after retrieving the restaurants from the database
                restaurantAdapter = RestaurantAdapter(restaurantsList)
                restaurantRV.adapter = restaurantAdapter
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "No restaurants in DB.", error.toException())
                Toast.makeText(requireActivity(), "No Restaurants Available.", Toast.LENGTH_SHORT).show()
            }
        })
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



