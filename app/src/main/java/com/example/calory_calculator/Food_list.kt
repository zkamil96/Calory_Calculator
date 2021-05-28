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
        food_list_name = findViewById(R.id.food_list_name)
        food_list_btn = findViewById(R.id.food_list_button)

        var intent: Intent = getIntent()
        var meal_name:String? = null
        meal_name = intent.getStringExtra("name")
        food_list_name?.text = meal_name

        var adapter = FoodAdapter(this)
        var recyclerView = findViewById<RecyclerView>(R.id.food_list_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        when (meal_name) {
            "breakfast" -> {
                adapter.setData(Variables.breakfast_list)
                Variables.b = true
                Variables.l = false
                Variables.s = false
                Variables.d = false
            }
            "lunchtime" -> {
                adapter.setDataLunch((Variables.lunchtime_list))
                Variables.b = false
                Variables.l = true
                Variables.s = false
                Variables.d = false
            }
            "snacks" -> {
                adapter.setDatasnacks((Variables.snacks_list))
                Variables.b = false
                Variables.l = false
                Variables.s = true
                Variables.d = false
            }
            "dinner" -> {
                adapter.setDatadinner((Variables.dinner_list))
                Variables.b = false
                Variables.l = false
                Variables.s = false
                Variables.d = true
            }
        }

        food_list_btn?.setOnClickListener{
            var intent = Intent(this, Search::class.java)
            startActivity(intent)
        }
    }
}