package com.example.calory_calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import io.realm.Realm
import io.realm.kotlin.syncSession
import io.realm.mongodb.sync.SyncConfiguration

class NoNetworkConnection : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_network_connection)
        var refresh_btn = findViewById<Button>(R.id.no_internet_btn)
        refresh_btn.setOnClickListener {
            if(Statistics.isWifiConnected(applicationContext)){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this,"No internet connection", Toast.LENGTH_SHORT).show()
            }
        }
    }
}