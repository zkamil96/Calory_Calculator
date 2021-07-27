package com.example.calory_calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calory_calculator.ADAPTER.FoodAdapter
import com.example.calory_calculator.ADAPTER.MyAdapter

class Food_list : AppCompatActivity(), FoodAdapter.OnItemClickListener {
    var food_list_name:TextView? = null
    var food_list_btn:ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)
        if(!Statistics.isWifiConnected(applicationContext)){
            val intent = Intent(this, NoNetworkConnection::class.java)
            startActivity(intent)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        food_list_name = findViewById(R.id.food_list_name)
        food_list_btn = findViewById(R.id.food_list_button)
        food_list_name?.text = Variables.meal_name

        var adapter = FoodAdapter(this)
        var recyclerView = findViewById<RecyclerView>(R.id.food_list_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        when (Variables.meal_name) {
            "breakfast" -> {
                adapter.setData(Variables.breakfast_list)
            }
            "lunchtime" -> {
                adapter.setDataLunch((Variables.lunchtime_list))
            }
            "snacks" -> {
                adapter.setDatasnacks((Variables.snacks_list))
            }
            "dinner" -> {
                adapter.setDatadinner((Variables.dinner_list))
            }
        }

        food_list_btn?.setOnClickListener{
            var intent = Intent(this, Search::class.java)
            startActivity(intent)
        }
    }
}