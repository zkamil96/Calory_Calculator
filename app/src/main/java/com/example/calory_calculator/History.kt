package com.example.calory_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calory_calculator.ADAPTER.HistoryAdapter
import com.example.calory_calculator.ADAPTER.MyAdapter
import com.example.calory_calculator.MODELS.calory_value
import com.example.calory_calculator.MODELS.history_value
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.sync.SyncConfiguration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

class History : AppCompatActivity(), HistoryAdapter.OnItemClickListener {
    val user = Variables.app?.currentUser()
    val config = SyncConfiguration
            .Builder(user, Variables.app?.currentUser()?.id)
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
    var realm : Realm = Realm.getInstance(config)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var adapter = HistoryAdapter(this)
        var recyclerView = findViewById<RecyclerView>(R.id.history_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        Handler(Looper.getMainLooper()).post {
            realm.executeTransaction {
                val dataFromProfile = it.where<history_value>().findAll()
                if (dataFromProfile != null) {
                    adapter.setData(dataFromProfile.sort("date"))
                    Log.v("history", "Data showed")
                } else {
                    Log.v("history", "Data not showed")
                }
            }
        }
    }

    override fun onItemClick(id: Long, name: String) {
        var actual_date = LocalDateTime.now()
        var date = Search.asDate(actual_date)
        var dialog = CustomDialogFragment()
        dialog.get_Values(id.toInt(), name, date!!)
        dialog.show(supportFragmentManager, "customDialog")
    }
}