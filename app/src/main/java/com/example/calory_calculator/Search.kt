package com.example.calory_calculator

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calory_calculator.ADAPTER.FavoriteListAdapter
import com.example.calory_calculator.ADAPTER.MyAdapter
import com.example.calory_calculator.API.ApiService
import com.example.calory_calculator.MODELS.Prod
import com.example.calory_calculator.MODELS.days_value
import com.example.calory_calculator.MODELS.favorite_list_value
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.sync.SyncConfiguration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatterBuilder
import java.util.*

class Search : AppCompatActivity(), MyAdapter.OnItemClickListener {
    val user = Variables.app?.currentUser()
    val config = SyncConfiguration
        .Builder(user, Variables.app?.currentUser()?.id)
        .allowQueriesOnUiThread(true)
        .allowWritesOnUiThread(true)
        .build()
    var realm : Realm = Realm.getInstance(config)
    var actual_calory:Long = 0
    var actual_fats:Long = 0
    var actual_carbohydrates:Long = 0
    var actual_proteins:Long = 0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        var adapter = MyAdapter(this)
        var recyclerView = findViewById<RecyclerView>(R.id.search_list_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        var search_value = findViewById<EditText>(R.id.search_value)
        var search_value_button = findViewById<ImageButton>(R.id.search_value_button)
        var favorite_list = findViewById<Button>(R.id.go_to_favorite_list_button)
        favorite_list.setOnClickListener {
            val intent = Intent(this, Favorite_products::class.java)
            startActivity(intent)
        }
        search_value_button.setOnClickListener(){
            if(search_value.text != null){
                var api = retrofit.create(ApiService::class.java)
                var call = api.getProducts(search_value.text.toString())
                Handler(Looper.getMainLooper()).post {
                    call?.enqueue(object : Callback<Prod> {
                        override fun onResponse(call: Call<Prod>, response: Response<Prod>) {
                            Log.v("Suc", "Success ")
                            if (response.body().toString() != null)
                                adapter.setData(response.body()!!.results)
                        }

                        override fun onFailure(call: Call<Prod>, t: Throwable) {
                            Log.v("Suc", t.message.toString())
                        }
                    })
                }
            }
        }

        var actual_date = LocalDate.now()
        var parse_date = Date.from(actual_date.atStartOfDay(ZoneId.systemDefault()).toInstant())
        realm.executeTransaction {
            val dataAboutProducts = it.where<days_value>().equalTo("date", parse_date).findFirst()
            if (dataAboutProducts != null) {
                if(dataAboutProducts.breakfast != null)
                for(prod in dataAboutProducts.breakfast){
                    actual_calory = actual_calory?.plus(prod.calories?.toLong()!!)
                    actual_fats = actual_fats?.plus(prod.fats?.toLong()!!)
                    actual_carbohydrates = actual_carbohydrates?.plus(prod.carbohydrates?.toLong()!!)
                    actual_proteins = actual_proteins?.plus(prod.proteins?.toLong()!!)
                    //Log.v("tak", dataAboutProducts.breakfast.toString())
                    //Log.v("tak", actual_calory.toString())
                }
                if(dataAboutProducts.lunchtime != null)
                for(prod in dataAboutProducts.lunchtime){
                    actual_calory = actual_calory?.plus(prod.calories?.toLong()!!)
                    actual_fats = actual_fats?.plus(prod.fats?.toLong()!!)
                    actual_carbohydrates = actual_carbohydrates?.plus(prod.carbohydrates?.toLong()!!)
                    actual_proteins = actual_proteins?.plus(prod.proteins?.toLong()!!)
                }
                if(dataAboutProducts.snacks != null)
                for(prod in dataAboutProducts.snacks){
                    actual_calory = actual_calory?.plus(prod.calories?.toLong()!!)
                    actual_fats = actual_fats?.plus(prod.fats?.toLong()!!)
                    actual_carbohydrates = actual_carbohydrates?.plus(prod.carbohydrates?.toLong()!!)
                    actual_proteins = actual_proteins?.plus(prod.proteins?.toLong()!!)
                }
                if(dataAboutProducts.dinner != null)
                for(prod in dataAboutProducts.dinner){
                    actual_calory = actual_calory?.plus(prod.calories?.toLong()!!)
                    actual_fats = actual_fats?.plus(prod.fats?.toLong()!!)
                    actual_carbohydrates = actual_carbohydrates?.plus(prod.carbohydrates?.toLong()!!)
                    actual_proteins = actual_proteins?.plus(prod.proteins?.toLong()!!)
                }
                dataAboutProducts.actual_calory = actual_calory
                dataAboutProducts.actual_carbohydrates = actual_carbohydrates
                dataAboutProducts.actual_fats = actual_fats
                dataAboutProducts.actual_proteins = actual_proteins
            }
        }

    }

    companion object{
        var retrofit = Retrofit.Builder()
                .baseUrl(ApiService.base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    override fun onItemClick(id: Int, name: String) {
        var actual_date = LocalDateTime.now()
        var date = asDate(actual_date)
        var dialog = CustomDialogFragment()
        dialog.get_Values(id, name, date!!)
        dialog.show(supportFragmentManager, "customDialog")
    }

    fun asDate(localDateTime: LocalDateTime): Date? {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    }


}