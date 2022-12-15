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
import com.example.wagba.order.Order
import com.example.wagba.order.OrderAdapter


class Order : Fragment() {

    private lateinit var orderRV: RecyclerView
    private lateinit var orderList:ArrayList<Order>
    private lateinit var orderAdapter: OrderAdapter

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

        orderRV = view.findViewById(R.id.orderRecyclerView)



        orderRV_init()


    }

    private fun orderRV_init(){
        orderRV.setHasFixedSize(true)
        orderRV.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(orderRV)

        orderList = ArrayList()
        addOrderToList()

        orderAdapter = OrderAdapter(orderList)
        orderRV.adapter = orderAdapter

    }
    private fun addOrderToList(){
        orderList.add(Order(R.drawable.mcdonalds_logo_504,this.resources.getString(R.string.orderDetails), "placed"))
        orderList.add(Order(R.drawable.starbucks_logo_504,this.resources.getString(R.string.orderDetails), "on delivery"))
        orderList.add(Order(R.drawable.sadam_logo_504,this.resources.getString(R.string.orderDetails), "delivered"))
        orderList.add(Order(R.drawable.kosharyeltahrir_logo_504,this.resources.getString(R.string.orderDetails), "delivered"))

    }




}