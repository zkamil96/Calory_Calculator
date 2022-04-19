package com.example.calory_calculator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.realm.mongodb.sync.SyncSession


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!Statistics.isWifiConnected(applicationContext)){
            val intent = Intent(this, NoNetworkConnection::class.java)
            startActivity(intent)
        }
        if(app.currentUser() != null) {
            val intent = Intent(this, Statistics::class.java)
            startActivity(intent)
        }
        setContentView(R.layout.activity_main)
        val login_button = findViewById<Button>(R.id.button)
        val register_button = findViewById<Button>(R.id.button2)

        register_button.setOnClickListener() {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        login_button.setOnClickListener() {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if(app.currentUser() != null) {
            val intent = Intent(this, Statistics::class.java)
            startActivity(intent)
        }
    }
}