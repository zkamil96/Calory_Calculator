package com.example.calory_calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calory_calculator.ADAPTER.FavoriteListAdapter
import com.example.calory_calculator.MODELS.favorite_list_value
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.sync.SyncConfiguration
import java.time.LocalDateTime

class Favorite_products : AppCompatActivity(), FavoriteListAdapter.OnItemClickListener {
/*    val user = app.currentUser()
    val config = SyncConfiguration
            .Builder(user, app.currentUser()?.id)
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
    var realm : Realm = Realm.getInstance(config)*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_products)
        if(!Statistics.isWifiConnected(applicationContext)){
            val intent = Intent(this, NoNetworkConnection::class.java)
            startActivity(intent)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var adapter = FavoriteListAdapter(this)
        var recyclerView = findViewById<RecyclerView>(R.id.favorite_products_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        Handler(Looper.getMainLooper()).post {
            realm?.executeTransaction {
                val dataFromProfile = it.where<favorite_list_value>().findAll()
                if (dataFromProfile != null) {
                    adapter.setData(dataFromProfile)
                    Log.v("favorite products", "Data showed")
                } else {
                    Log.v("favorite products", "Data not showed")
                }
            }
        }
    }

    override fun onFavItemClick(id: Long, name: String) {
        var actual_date = LocalDateTime.now()
        var date = Search.asDate(actual_date)
        var dialog = CustomDialogFragment()
        dialog.get_Values(id.toInt(), name, date!!)
        dialog.show(supportFragmentManager, "customDialog")
    }
}
