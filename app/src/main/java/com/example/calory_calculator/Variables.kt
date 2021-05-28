package com.example.calory_calculator

import com.example.calory_calculator.MODELS.*
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.User
import java.time.LocalDate

object Variables {
    var app: App? = null
    //val app: App = App(AppConfiguration.Builder(app_Id).build())
    var clear_or_not : Boolean = true
    var favorite_product_list = emptyList<Int>()
    var history_product_list = emptyList<Int>()
    //var choosen_date = LocalDate.now().toString()
    //var realm: Realm = Realm.getDefaultInstance()
    var breakfast_list = emptyList<days_value_breakfast>()
    var lunchtime_list = emptyList<days_value_lunchtime>()
    var snacks_list = emptyList<days_value_snacks>()
    var dinner_list = emptyList<days_value_dinner>()
    var b = false
    var l = false
    var s = false
    var d = false
}