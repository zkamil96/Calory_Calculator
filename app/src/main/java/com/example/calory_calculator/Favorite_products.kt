package com.example.calory_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Favorite_products : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_products)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}