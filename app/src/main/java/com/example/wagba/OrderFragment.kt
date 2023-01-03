package com.example.wagba

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.wagba.dish.Dish
import com.example.wagba.dish.DishAdapter
import com.example.wagba.order.Order
import com.example.wagba.order.OrderAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class OrderFragment : Fragment() {

    private lateinit var orderRV: RecyclerView
    private lateinit var orderList:ArrayList<Order>
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var userId: String
    private lateinit var database: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var orderRefList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance("https://wagba-db-default-rtdb.firebaseio.com/")
        firebaseAuth = FirebaseAuth.getInstance()
        userId = firebaseAuth.currentUser!!.uid
        orderRefList = mutableListOf()


        orderRV = view.findViewById(R.id.orderRecyclerView)
        orderRV_init()
    }

    private fun orderRV_init(){
        orderRV.setHasFixedSize(true)
        orderRV.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(orderRV)

        orderList = ArrayList()
        getOrdersRef()




    }
private fun getOrdersRef(){

    val userOrderRef = database.getReference("users").child(userId).child("Orders")
    userOrderRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val orderList: MutableList<String> = ArrayList()
                val ordersRef = dataSnapshot.children.mapNotNull { snapshot ->
                    val orderRef = snapshot.getValue(String::class.java)
                    orderRef
                }
                orderList.addAll(ordersRef)
                // set adapter after retrieving the dishes from the database
                callbackGetOrdersRef(orderList)
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "No orders in DB.", error.toException())
                Toast.makeText(requireActivity(), "No Orders Available.", Toast.LENGTH_SHORT).show()
            }
        })
    }
private fun callbackGetOrdersRef(l: MutableList<String>){
    orderRefList = l
    val ordersRef = database.getReference("orders")
    for (orderRef in orderRefList) {
        ordersRef.child(orderRef).orderByChild("currentTime/time").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val order = snapshot.getValue(Order::class.java)
                //replace order if found
                //replace order if found
                orderList.filter() { it.orderId == order!!.orderId }.forEach() { it ->
                    orderList.remove(it)
                }
                orderList.add(order!!)
                val tempList = orderList.reversed()

                orderAdapter = OrderAdapter(tempList)
                orderRV.adapter = orderAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }
}
}