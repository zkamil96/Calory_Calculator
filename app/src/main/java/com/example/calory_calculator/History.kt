package com.example.calory_calculator

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calory_calculator.ADAPTER.HistoryAdapter
import com.example.calory_calculator.MODELS.history_value
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.sync.SyncConfiguration
import java.time.LocalDateTime


class History : AppCompatActivity(), HistoryAdapter.OnItemClickListener {
/*    val user = app.currentUser()
    val config = SyncConfiguration
            .Builder(user, app.currentUser()?.id)
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
    var realm : Realm = Realm.getInstance(config)*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        if(!Statistics.isWifiConnected(applicationContext)){
            val intent = Intent(this, NoNetworkConnection::class.java)
            startActivity(intent)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var adapter = HistoryAdapter(this)
        var recyclerView = findViewById<RecyclerView>(R.id.history_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        Handler(Looper.getMainLooper()).post {
            realm?.executeTransaction {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.history_menu -> {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)

                builder.setTitle("Confirm clean history")
                builder.setMessage("Are you sure you want clean history?")

                builder.setPositiveButton("YES", DialogInterface.OnClickListener { dialog, which -> // Do nothing but close the dialog
                    realm?.executeTransaction {
                        val dataFromProfile = it.where<history_value>().findAll()
                        if (dataFromProfile != null) {
                            dataFromProfile.deleteAllFromRealm()
                            Log.v("history", "Data showed")
                            finish()
                            startActivity(intent)
                        } else {
                            Log.v("history", "Data not showed")
                        }
                    }
                    dialog.dismiss()
                })

                builder.setNegativeButton("NO", DialogInterface.OnClickListener { dialog, which -> // Do nothing
                    dialog.dismiss()
                })

                val alert: AlertDialog = builder.create()
                alert.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}