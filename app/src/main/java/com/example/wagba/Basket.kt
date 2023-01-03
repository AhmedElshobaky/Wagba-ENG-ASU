package com.example.wagba

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.wagba.data.*
import com.example.wagba.dish.BasketDishAdapter
import com.example.wagba.dish.Dish
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.Serializable
import com.example.wagba.order.Order
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.reflect.typeOf

class Basket : Fragment() {

    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner

    private lateinit var basketDishesRV: RecyclerView
    private lateinit var basketDishesAdapter: BasketDishAdapter
    private lateinit var basketDishes: List<Dish>

    //todo: restaurant name,username, order id, total amount, gate, time
    private lateinit var restaurantNameTv: TextView
    private lateinit var userNameTv: TextView
    private var totalAmount: Double = 0.0
    private lateinit var totalTv: TextView

    private lateinit var placeOrderBtn: Button

    private lateinit var database: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth

    companion object {
        fun newInstance(
            restaurantId: String,
            restaurantName: String,
            restaurantLogo: String,
            dishList: List<Dish>
        ): Basket {
            val fragment = Basket()
            val args = Bundle()
            args.putString("restaurantId", restaurantId)
            args.putString("restaurantName", restaurantName)
            args.putString("restaurantLogo", restaurantLogo)
            args.putSerializable("dishes", dishList as Serializable)

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
        return inflater.inflate(R.layout.fragment_basket, container, false)
    }


    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance("https://wagba-db-default-rtdb.firebaseio.com/")
        firebaseAuth = FirebaseAuth.getInstance()
        restaurantNameTv = view.findViewById(R.id.restaurantBasket)
        restaurantNameTv.text = "${arguments?.getString("restaurantName")}"
        userNameTv = view.findViewById(R.id.usernameBasket)

        getUserName() { username ->
            userNameTv.text = "Username: $username"

        }

        // calculate total amount
        @Suppress("UNCHECKED_CAST")
        basketDishes = arguments?.getSerializable("dishes") as List<Dish>
        for (dish in basketDishes) {
            totalAmount += dish.Price
        }
        totalTv = view.findViewById(R.id.totalTxt)
        val totalAmountStr: String = "Total: $totalAmount EGP"
        totalTv.text = totalAmountStr




        spinner1 = view.findViewById(R.id.gatesspinnerView)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gates,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner1.adapter = adapter
        }
        spinner2 = view.findViewById(R.id.timespinnerView)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.times,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner2.adapter = adapter
        }
        @Suppress("UNCHECKED_CAST")
        basketDishes = arguments?.getSerializable("dishes") as MutableList<Dish>
        basketDishesRV = view.findViewById(R.id.basketDishesRecyclerView)
        basketDishesRV_init()

        placeOrderBtn = view.findViewById(R.id.placeOrder)
        placeOrderBtn.setOnClickListener {
            placeOrder()
        }

    }

    private fun placeOrder() {
        // Customers who need a delivery at 12:00 noon must place an order before 10:00 am.
        //Customers who need a delivery at 3:00 pm must order before 1:00 pm.
        val time = spinner2.selectedItem.toString()
        val currentCalendar = Calendar.getInstance().time

        /*
        if (time =="Noon delivery" && (currentCalendar.hours > 10 || currentCalendar.hours < 6)) {
            Toast.makeText(
                requireContext(),
                "You can't place an order now for Noon delivery!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }else if (time == "3:00 pm delivery" &&(currentCalendar.hours >13 || currentCalendar.hours < 6)) {
            Toast.makeText(
                requireContext(),
                "You can't place an order now for 3:00 pm delivery!",
                Toast.LENGTH_SHORT
            ).show()
            return
        */

        val restaurantId = arguments?.getString("restaurantId")
        val restaurantLogo = arguments?.getString("restaurantLogo")
        val userId = getUserId()
        val dishes = arguments?.getSerializable("dishes") as List<Dish>
        val gate = spinner1.selectedItem.toString()
        val totalAmount = totalAmount

        val orderId =(currentCalendar.time).toString()
        val order = Order(
            orderId,
            restaurantId!!,
            userId,
            dishes,
            totalAmount,
            gate,
            time,
            "placed",
            currentCalendar,
            restaurantLogo!!
        )
        val ordersRef = database.getReference("orders")
        val userRef = database.getReference("users")
        val restaurantRef = database.getReference("Restaurant")
        ordersRef.child(orderId).setValue(order)
        userRef.child(userId).child("Orders").child(orderId).setValue(orderId)
        restaurantRef.child(restaurantId).child("Orders").child(orderId).setValue(orderId)
        Toast.makeText(requireContext(), "Order Placed", Toast.LENGTH_SHORT).show()
        toDeliveryFragment()

    }
    private fun toDeliveryFragment() {
        val fragment = Delivery()
        val fragmentManager = (context as AppCompatActivity).supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
    }

    private fun getUserId(): String {
        return firebaseAuth.currentUser!!.uid

    }

    private fun getUserName(callback: (String) -> Unit){
        val uid = firebaseAuth.currentUser!!.uid
        Log.d("BasketDebuggingLog", "uid: $uid")
        val ref = database.getReference("users").child(uid).child("username")
        Log.d("BasketDebuggingLog", "uid: $ref")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username = dataSnapshot.getValue(String::class.java)!!
                Log.d("BasketDebuggingLog", "uid: $username")
                callback(username)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed get the username", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun basketDishesRV_init() {
        basketDishesRV.setHasFixedSize(true)
        basketDishesRV.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(basketDishesRV)
        val (uniqueDishes, frequency) = getUniqueDishesAndFrequency(basketDishes)
        basketDishesAdapter = BasketDishAdapter(uniqueDishes, frequency)
        basketDishesRV.adapter = basketDishesAdapter
    }

    private fun getUniqueDishesAndFrequency(list: List<Dish>): Pair<List<Dish>, List<Int>> {
        val uniqueDishes = mutableListOf<Dish>()
        val frequency = mutableListOf<Int>()
        for (dish in list) {
            if (uniqueDishes.contains(dish)) {
                frequency[uniqueDishes.indexOf(dish)] += 1
            } else {
                uniqueDishes.add(dish)
                frequency.add(1)
            }
        }
        return Pair(uniqueDishes, frequency)
    }

}
