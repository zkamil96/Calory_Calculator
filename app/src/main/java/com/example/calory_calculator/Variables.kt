package com.example.calory_calculator

import com.example.calory_calculator.MODELS.*
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.User
import java.time.LocalDate

object Variables {
    var app: App? = null
    var clear_or_not : Boolean = true
    var choosen_date = LocalDate.now()
    var meal_name:String? = null
    var fav_or_not : Boolean = true
}