package com.example.wagba

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.wagba.dish.Dish
import com.example.wagba.dish.DishAdapter



class RestaurantDishes : Fragment() {

    private lateinit var dishRV: RecyclerView
    private lateinit var dishList:ArrayList<Dish>
    private lateinit var dishAdapter: DishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_restaurant_dishes, container, false)
        dishRV = view.findViewById(R.id.dishesRecyclerView)
        dish_init()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //dishRV = view.findViewById(R.id.dishesRecyclerView)
        //dish_init()


    }
    private fun dish_init(){
        dishRV.setHasFixedSize(true)
        dishRV.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(dishRV)

        dishList = ArrayList()
        addDishToList()

        dishAdapter = DishAdapter(dishList)
        dishRV.adapter = dishAdapter
    }
    private fun addDishToList(){
        dishList.add(Dish(R.drawable.ic_launcher_background,this.resources.getString(R.string.mc_dish1), 190.0))
        dishList.add(Dish(R.drawable.ic_launcher_background,this.resources.getString(R.string.mc_dish2), 90.0))
        dishList.add(Dish(R.drawable.ic_launcher_background,this.resources.getString(R.string.mc_dish3), 70.0))
        dishList.add(Dish(R.drawable.ic_launcher_background,this.resources.getString(R.string.mc_dish4), 20.0))
    }
}