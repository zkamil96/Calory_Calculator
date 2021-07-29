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
import com.example.calory_calculator.MODELS.*
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.sync.SyncConfiguration
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class Food_list : AppCompatActivity(), FoodAdapter.OnItemClickListener {
    var food_list_name:TextView? = null
    var food_list_btn:ImageButton? = null
    val user = Variables.app?.currentUser()
    val config = SyncConfiguration
            .Builder(user, Variables.app?.currentUser()?.id)
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
    var realm : Realm = Realm.getInstance(config)

    var actual_date = Variables.choosen_date
    var parse_date = Date.from(actual_date.atStartOfDay(ZoneId.systemDefault()).toInstant())

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

        realm.executeTransaction {
            val dataFromProfile = it.where<days_value>().equalTo("date", parse_date).findFirst()
            var breakfast_list_temp = emptyList<days_value_breakfast>()
            var lunchtime_list_temp = emptyList<days_value_lunchtime>()
            var snacks_list_temp = emptyList<days_value_snacks>()
            var dinner_list_temp = emptyList<days_value_dinner>()
            if(dataFromProfile != null){
                if(dataFromProfile.breakfast.size > 0){
                    breakfast_list_temp = dataFromProfile.breakfast
                }
                if(dataFromProfile.lunchtime.size > 0){
                    lunchtime_list_temp = dataFromProfile.lunchtime
                }
                if(dataFromProfile.snacks.size > 0){
                    snacks_list_temp = dataFromProfile.snacks
                }
                if(dataFromProfile.dinner.size > 0){
                    dinner_list_temp = dataFromProfile.dinner
                }
            }
            when (Variables.meal_name) {
                "breakfast" -> {
                    adapter.setData(breakfast_list_temp)
                }
                "lunchtime" -> {
                    adapter.setDataLunch((lunchtime_list_temp))
                }
                "snacks" -> {
                    adapter.setDatasnacks((snacks_list_temp))
                }
                "dinner" -> {
                    adapter.setDatadinner((dinner_list_temp))
                }
            }
        }

        food_list_btn?.setOnClickListener{
            var intent = Intent(this, Search::class.java)
            startActivity(intent)
        }
    }
}