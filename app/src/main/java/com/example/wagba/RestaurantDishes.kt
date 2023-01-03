package com.example.wagba

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.example.wagba.dish.Dish
import com.example.wagba.dish.DishAdapter
import com.example.wagba.restaurant.Restaurant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class RestaurantDishes : Fragment() {

    private lateinit var restaurantNameTv: TextView
    private lateinit var restaurantView: ImageView
    private lateinit var dishRV: RecyclerView
    private lateinit var dishAdapter: DishAdapter

    private lateinit var database: FirebaseDatabase


    private var dishesList = mutableListOf<Dish>()

    private lateinit var basketBtn: Button


    //todo : add "add dish to basket functionality, on click listener for basket -> basket fragment"
    companion object {
        fun newInstance(restaurant: Restaurant): RestaurantDishes {
            val fragment = RestaurantDishes()
            val args = Bundle()
            args.putString("restaurantId", restaurant.restaurantId)
            args.putString("restaurantName", restaurant.Name)
            args.putString("restaurantLogo", restaurant.imgUrl)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurant_dishes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set text of view element to restaurant name
        restaurantNameTv = view.findViewById(R.id.restaurantName)
        restaurantView = view.findViewById(R.id.restaurantView)
        restaurantNameTv.text = arguments?.getString("restaurantName")
        Glide.with(restaurantView.context)
            .load(arguments?.getString("restaurantLogo"))
            .into(restaurantView)

        dishRV = view.findViewById(R.id.dishesRecyclerView)
        database = FirebaseDatabase.getInstance("https://wagba-db-default-rtdb.firebaseio.com/")
        dish_init()

        basketBtn = view.findViewById(R.id.basketBtn)
        basketBtn.setOnClickListener {
            val basketDishes: List<Dish> = dishAdapter.getBasketDishes()
            if (basketDishes.isNotEmpty()) {
                Log.d("dishesLog", basketDishes.toString())
                val basketFragment = Basket.newInstance(restaurantId = arguments?.getString("restaurantId").toString(), restaurantName = arguments?.getString("restaurantName").toString(), restaurantLogo = arguments?.getString("restaurantLogo").toString() , basketDishes)
                val fragmentManager = (context as AppCompatActivity).supportFragmentManager
                fragmentManager.beginTransaction().replace(R.id.frameLayout, basketFragment).commit()
            } else {
                Toast.makeText(context, "Basket is empty", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun dish_init(){
        dishRV.setHasFixedSize(true)
        dishRV.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(dishRV)

        dishesList = ArrayList()
        addDishesFromDB()
    }
    private fun addDishesFromDB() {
        val restaurantId = arguments?.getString("restaurantId").toString()
        //firebase/Restaurant/00/Dishes
        val dishesRef = database.getReference("Restaurant/$restaurantId/Dishes")
        dishesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dishes = dataSnapshot.children.mapNotNull { snapshot ->
                    val dish = snapshot.getValue(Dish::class.java)
                    Log.d("addDishesFromDB", "dish: $dish)")
                    dish
                }
                Log.d("addDishesFromDB", "dishes: $dishes)")
                dishesList.addAll(dishes)
                // set adapter after retrieving the dishes from the database
                dishAdapter = DishAdapter(dishesList)
                dishRV.adapter = dishAdapter
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "No dishes in DB.", error.toException())
                Toast.makeText(requireActivity(), "No Dishes Available.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}