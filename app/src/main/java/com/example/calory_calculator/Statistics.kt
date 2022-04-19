package com.example.calory_calculator

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.calory_calculator.MODELS.calory_value
import com.example.calory_calculator.MODELS.days_value
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import org.bson.types.ObjectId
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class Statistics : AppCompatActivity(), ChooseDateInterface{
    var calculated_calory:Double? = 0.0
    var calculated_fats:Double? = 0.0
    var calculated_carbohydrates:Double? = 0.0
    var calculated_proteins:Double? = 0.0
    var actual_calory: Int? = 0
    var actual_fats: Int? = 0
    var actual_carbohydrates: Int? = 0
    var actual_proteins: Int? = 0
    var ppm_value: Double? = null
    var cpm_value: Double? = null
    var gender_value: String? = null
    var growth_value: String? = null
    var weight_value: String? = null
    var age_value: String? = null
    var physical_activity_value: String? = null
    var destination_value: String? = null
    var calory_number:TextView? = null
    var fats_number:TextView? = null
    var carbohydrates_number:TextView? = null
    var proteins_number:TextView? = null
    var date_btn:Button? = null
    var breakfast_btn:Button? = null
    var lunchtime_btn:Button? = null
    var snacks_btn:Button? = null
    var dinner_btn:Button? = null
    var parse_date:Date? = null
    var dec_calory:Long = 0
    var dec_proteins:Long = 0
    var dec_fats:Long = 0
    var dec_carbohydrates:Long = 0
    var sum_calory:Long = 0
    var sum_fats:Long = 0
    var sum_carbohydrates:Long = 0
    var sum_proteins:Long = 0
    var actual_cups_of_water:Long = 0
    var actual_amount_of_cups:Int  = 0
    var water_layout :GridLayout? = null
    var glassFillStart: AnimationDrawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        user = app.currentUser()!!
        val config = SyncConfiguration
                .Builder(user, user?.id)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build()
        realm = Realm.getInstance(config)
            if(!isWifiConnected(applicationContext)){
                val intent = Intent(this, NoNetworkConnection::class.java)
                startActivity(intent)
            }

        Variables.fav_or_not = false
        if(!realm?.isAutoRefresh!!){
            realm?.refresh()
        }

        water_layout = findViewById(R.id.water_cups_layout)
        calory_number = findViewById(R.id.calory_text)
        fats_number = findViewById(R.id.fats_text)
        carbohydrates_number = findViewById(R.id.carbohydrates_text)
        proteins_number = findViewById(R.id.proteins_text)
        date_btn = findViewById(R.id.date_button)
        date_btn?.text = Variables.choosen_date.toString()
        breakfast_btn = findViewById(R.id.breakfast_button)
        lunchtime_btn = findViewById(R.id.lunchtime_button)
        snacks_btn = findViewById(R.id.snacks_button)
        dinner_btn = findViewById(R.id.dinner_button)
        var dialog = Date_Dialog()

        date_btn?.setOnClickListener {
            if(!dialog.isAdded()){
                dialog.show(supportFragmentManager, "DateDialog")
            }
        }

        val parse_date_local = LocalDate.parse(date_btn?.text, DateTimeFormatter.ISO_DATE)
        parse_date = Date.from(parse_date_local.atStartOfDay(ZoneId.systemDefault()).toInstant())
        showActualDataFromDB()

        val listener = View.OnClickListener { view ->
            changeMealName(view)
            val intent = Intent(this, Food_list::class.java)
            startActivity(intent)
        }

        breakfast_btn?.setOnClickListener(listener)
        lunchtime_btn?.setOnClickListener(listener)
        snacks_btn?.setOnClickListener(listener)
        dinner_btn?.setOnClickListener(listener)

            realm?.executeTransaction{
                val dataFromProfile = it.where<calory_value>().equalTo("owner_id", user?.id).findFirst()
                if(dataFromProfile != null) {
                    gender_value = dataFromProfile.gender
                    growth_value = dataFromProfile.growth.toString()
                    weight_value = dataFromProfile.weight.toString()
                    age_value = dataFromProfile.age.toString()
                    physical_activity_value = dataFromProfile.physical_activity
                    destination_value = dataFromProfile.destination
                    Log.v("Success", "successfuly found a document")
                    if(gender_value != null && growth_value != null && weight_value != null && age_value != null && physical_activity_value != null && destination_value != null){
                        if(gender_value == "Male"){
                            ppm_value =  (9.99 * weight_value!!.toDouble()) + (6.25 * growth_value!!.toDouble()) - (4.92 * age_value!!.toDouble()) + 5
                        }else if(gender_value == "Female"){
                            ppm_value = (9.99 * weight_value!!.toDouble()) + (6.25 * growth_value!!.toDouble())  - (4.92 * age_value!!.toDouble()) - 161
                        }
                        cpm_value = ppm_value!! * PAI_value(physical_activity_value!!)
                        cpm_value = cpm_value!! + destination_value_calculated(destination_value!!)
                        calculated_calory = cpm_value
                        calculated_proteins = (calculated_calory!! * 0.15) /4
                        calculated_fats = (calculated_calory!! * 0.25) /9
                        calculated_carbohydrates = (calculated_calory!! - (calculated_fats!! + calculated_proteins!!)) /4
                    }else{
                        calculated_calory = 0.0
                        calculated_carbohydrates = 0.0
                        calculated_fats = 0.0
                        calculated_proteins = 0.0
                    }
                    dec_calory = Math.round(calculated_calory!!)
                    dec_proteins = Math.round(calculated_proteins!!)
                    dec_fats = Math.round(calculated_fats!!)
                    dec_carbohydrates= Math.round(calculated_carbohydrates!!)

                    calory_number?.text = "Kcal \n\n $actual_calory/$dec_calory kcal"
                    fats_number?.text = " Fats \n\n $actual_fats/$dec_fats g"
                    carbohydrates_number?.text = " Carbohydrates \n\n $actual_carbohydrates/$dec_carbohydrates g"
                    proteins_number?.text = " Proteins \n\n $actual_proteins/$dec_proteins g"

                } else {
                    Log.e("Failed", "failed to find document")
                    if(Variables.clear_or_not){
                        var preferences = PreferenceManager.getDefaultSharedPreferences(this)
                        var editor = preferences?.edit()
                        editor?.clear()
                        editor?.apply()
                    }
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)
                }
            }
    }

    override fun onStart() {
        super.onStart()
        calculateCupsCount()
        generateImageLayout(actual_amount_of_cups)

    }

    private fun calculateCupsCount(){
        if(actual_cups_of_water.toInt() == 0){
            actual_amount_of_cups = 1
        }else{
            actual_amount_of_cups = actual_cups_of_water.toInt() + 1
        }
    }

    private fun generateImageLayout(size: Int){
        water_layout?.removeAllViews()
        var column = 4
        var row = (size + 4 - 1)/4
        water_layout?.columnCount = column
        water_layout?.rowCount = row

            var i = 0
            var c = 0
            var r = 0
            while (i < size) {
                if (c == column) {
                    c = 0
                    r++
                }
                val oImageView = ImageView(this)
                    oImageView.id = i
                if(i + 1 == size){
                    oImageView.apply {
                        this.setBackgroundResource(R.drawable.fill_bottle_animation)
                        glassFillStart = background as AnimationDrawable
                    }
                }else{
                    oImageView.apply {
                        this.setBackgroundResource(R.drawable.unfill_bottle_animation)
                        glassFillStart = background as AnimationDrawable
                    }
                }
                val param: GridLayout.LayoutParams = GridLayout.LayoutParams()
                param.height = 200
                param.width = 200
                param.leftMargin = 60
                param.topMargin = 30
                param.setGravity(Gravity.CENTER)
                param.columnSpec = GridLayout.spec(c)
                param.rowSpec = GridLayout.spec(r)
                oImageView.layoutParams = param

                oImageView.setOnClickListener {
                    if(it.id == size - 1){
                        glassFillStart?.start()
                        actual_cups_of_water++
                        realm?.executeTransaction {
                            var dataAboutProducts = it.where<days_value>().equalTo("date", parse_date).findFirst()
                            if(dataAboutProducts != null){
                                dataAboutProducts.cups_of_water = actual_cups_of_water
                            }
                        }
                        calculateCupsCount()
                        generateImageLayout(actual_amount_of_cups)

                    }
                    if(it.id < size - 1){
                        it.apply {
                            this.setBackgroundResource(R.drawable.unfill_bottle_animation)
                            glassFillStart = background as AnimationDrawable
                        }
                        glassFillStart?.start()
                        actual_cups_of_water = it.id.toLong()
                        realm?.executeTransaction {
                            var dataAboutProducts = it.where<days_value>().equalTo("date", parse_date).findFirst()
                            if(dataAboutProducts != null){
                                dataAboutProducts.cups_of_water = actual_cups_of_water
                            }
                        }
                        calculateCupsCount()
                        generateImageLayout(actual_amount_of_cups)
                    }
                }
                water_layout?.addView(oImageView)
                i++
                c++
            }
        water_layout?.refreshDrawableState()
    }

    companion object {
        fun isWifiConnected(context: Context?): Boolean {
            val cm = context!!.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            if (context != null) {
                if (Build.VERSION.SDK_INT < 23) {
                    val mWiFiNetworkInfo = cm.activeNetworkInfo
                    if (mWiFiNetworkInfo != null) {
                        if (mWiFiNetworkInfo.type == ConnectivityManager.TYPE_WIFI) { //WIFI
                            return true
                        } else if (mWiFiNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) { //Mobile data
                            return true
                        }
                    }
                } else {
                    val network = cm.activeNetwork
                    if (network != null) {
                        val nc = cm.getNetworkCapabilities(network)
                        if (nc != null) {
                            if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) { //WIFI
                                return true
                            } else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) { //Mobile data
                                return true
                            }
                        }
                    }
                }
            }
            return false
        }
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).post {
            if(Variables.clear_or_not){
                var preferences = PreferenceManager.getDefaultSharedPreferences(this)
                var editor = preferences?.edit()
                editor?.clear()
                editor?.apply()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm?.close()
    }

    private fun showActualDataFromDB() {
            realm?.executeTransaction{
                var dataAboutProducts = it.where<days_value>().equalTo("date", parse_date).findFirst()
                if (dataAboutProducts != null) {
                    if (dataAboutProducts.breakfast.size > 0)
                        for (prod in dataAboutProducts.breakfast) {
                            sum_calory = sum_calory?.plus(prod.calories?.toLong()!!)
                            sum_fats = sum_fats?.plus(prod.fats?.toLong()!!)
                            sum_carbohydrates = sum_carbohydrates?.plus(prod.carbohydrates?.toLong()!!)
                            sum_proteins = sum_proteins?.plus(prod.proteins?.toLong()!!)
                        }
                    if (dataAboutProducts.lunchtime.size > 0)
                        for (prod in dataAboutProducts.lunchtime) {
                            sum_calory = sum_calory?.plus(prod.calories?.toLong()!!)
                            sum_fats = sum_fats?.plus(prod.fats?.toLong()!!)
                            sum_carbohydrates = sum_carbohydrates?.plus(prod.carbohydrates?.toLong()!!)
                            sum_proteins = sum_proteins?.plus(prod.proteins?.toLong()!!)
                        }
                    if (dataAboutProducts.snacks.size > 0)
                        for (prod in dataAboutProducts.snacks) {
                            sum_calory = sum_calory?.plus(prod.calories?.toLong()!!)
                            sum_fats = sum_fats?.plus(prod.fats?.toLong()!!)
                            sum_carbohydrates = sum_carbohydrates?.plus(prod.carbohydrates?.toLong()!!)
                            sum_proteins = sum_proteins?.plus(prod.proteins?.toLong()!!)
                        }
                    if (dataAboutProducts.dinner.size > 0)
                        for (prod in dataAboutProducts.dinner) {
                            sum_calory = sum_calory?.plus(prod.calories?.toLong()!!)
                            sum_fats = sum_fats?.plus(prod.fats?.toLong()!!)
                            sum_carbohydrates = sum_carbohydrates?.plus(prod.carbohydrates?.toLong()!!)
                            sum_proteins = sum_proteins?.plus(prod.proteins?.toLong()!!)
                        }
                    dataAboutProducts.actual_calory = sum_calory
                    dataAboutProducts.actual_carbohydrates = sum_carbohydrates
                    dataAboutProducts.actual_fats = sum_fats
                    dataAboutProducts.actual_proteins = sum_proteins

                    actual_cups_of_water = dataAboutProducts.cups_of_water!!
                    actual_calory = dataAboutProducts.actual_calory?.toInt()
                    actual_fats = dataAboutProducts.actual_fats?.toInt()
                    actual_carbohydrates = dataAboutProducts.actual_carbohydrates?.toInt()
                    actual_proteins = dataAboutProducts.actual_proteins?.toInt()
                    Log.v("Success", "Succesfully get data from db")
                } else {
                    val day_data = it.createObject(days_value::class.java, ObjectId())
                    day_data.owner_id = user?.id
                    day_data.actual_calory = 0
                    day_data.actual_carbohydrates = 0
                    day_data.actual_fats = 0
                    day_data.actual_proteins = 0
                    day_data.date = parse_date
                    day_data.cups_of_water = 0
                    Log.v("Failed", "Succesfully insert data to db")
                }
            }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.chart_button -> {
                val intent = Intent(this, Graph::class.java)
                startActivity(intent)
            }
            R.id.item1 -> {
                val intent = Intent(this, Profile::class.java)
                startActivity(intent)
            }
            R.id.item3 -> {
                val intent = Intent(this, Search::class.java)
                startActivity(intent)
            }
            R.id.item4 -> {
                val intent = Intent(this, Favorite_products::class.java)
                Variables.fav_or_not = true
                startActivity(intent)
            }
            R.id.item5 -> {
                val intent = Intent(this, History::class.java)
                startActivity(intent)
            }
            R.id.item6 -> {
                if (user?.isLoggedIn!!) {
                    user?.logOutAsync {
                        if (it.isSuccess) {
                            Log.v("Logout", "Logout Succesull")
                            val intent = Intent(this, MainActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        } else {
                            Log.v("Logout", it.error.toString())
                        }
                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun PAI_value(physical_activity: String): Double{
        var pai_calculated: Double? = null
        if(physical_activity == "Without physical activity"){
            pai_calculated = 1.2
        }else if(physical_activity == "Low physical activity"){
            pai_calculated = 1.4
        }else if(physical_activity == "Moderate physical activity"){
            pai_calculated = 1.6
        }else if(physical_activity == "High physical activity"){
            pai_calculated = 1.75
        }else if(physical_activity == "Very high physical activity"){
            pai_calculated =  2.0
        }
        return pai_calculated!!
    }

    fun destination_value_calculated(destination: String): Double{
        var destination_calculated: Double? = null
        if(destination == "Get weight"){
            destination_calculated = 250.0
        }else if(destination == "Maintain current weight"){
            destination_calculated = 0.0
        }else if(destination == "Lose weight"){
            destination_calculated = 400.0
        }
            return destination_calculated!!
    }

    private fun changeMealName(view: View) {
        when (view.getId()) {
            R.id.breakfast_button -> {
                Variables.meal_name = "breakfast"
            }
            R.id.lunchtime_button -> {
                Variables.meal_name = "lunchtime"
            }
            R.id.snacks_button -> {
                Variables.meal_name = "snacks"
            }
            R.id.dinner_button -> {
                Variables.meal_name = "dinner"
            }
        }
    }

    override fun applyDate(choosenDate: String) {
        date_btn?.text = choosenDate
        val parse_date_local = LocalDate.parse(date_btn?.text, DateTimeFormatter.ISO_DATE)
        Variables.choosen_date = parse_date_local
        parse_date = Date.from(parse_date_local.atStartOfDay(ZoneId.systemDefault()).toInstant())
        actual_cups_of_water = 0
        actual_calory = 0
        actual_fats = 0
        actual_carbohydrates = 0
        actual_proteins = 0
        realm?.executeTransaction{
            val dataAboutProducts = it.where<days_value>().equalTo("date", parse_date).findFirst()
            if(dataAboutProducts != null){
                actual_cups_of_water = dataAboutProducts.cups_of_water!!
                actual_calory = dataAboutProducts.actual_calory?.toInt()
                actual_fats = dataAboutProducts.actual_fats?.toInt()
                actual_carbohydrates = dataAboutProducts.actual_carbohydrates?.toInt()
                actual_proteins = dataAboutProducts.actual_proteins?.toInt()
                Log.v("Success", "Succesfully get data from db")
            }else{
                val day_data = it.createObject(days_value::class.java, ObjectId())
                day_data.owner_id = user?.id
                day_data.actual_calory = 0
                day_data.actual_carbohydrates = 0
                day_data.actual_fats = 0
                day_data.actual_proteins = 0
                day_data.date = parse_date
                day_data.cups_of_water = 0
                Log.v("Failed", "Succesfully insert data to db")
            }
        }
        calory_number?.text = "Kcal \n\n $actual_calory/$dec_calory kcal"
        fats_number?.text = " Fats \n\n $actual_fats/$dec_fats g"
        carbohydrates_number?.text = " Carbohydrates \n\n $actual_carbohydrates/$dec_carbohydrates g"
        proteins_number?.text = " Proteins \n\n $actual_proteins/$dec_proteins g"
        calculateCupsCount()
        generateImageLayout(actual_amount_of_cups)
    }
}