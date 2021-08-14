package com.example.calory_calculator

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.text.isDigitsOnly
import androidx.core.view.get
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import com.example.calory_calculator.API.ApiService
import com.example.calory_calculator.MODELS.*
import com.example.calory_calculator.Search.Companion.retrofit
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.sync.SyncConfiguration
import org.bson.types.ObjectId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class CustomDialogFragment: DialogFragment() {
    val user = Variables.app?.currentUser()
    val config = SyncConfiguration
            .Builder(user, Variables.app?.currentUser()?.id)
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
    var realm : Realm = Realm.getInstance(config)
    var id_values:Int? = null
    var name_values:String? = null
    var calory_amount:String? = "0.0"
    var proteins_amount:String? = "0.0"
    var fats_amount:String? = "0.0"
    var carbohydrates_amount:String? = "0.0"
    var search_date: Date? = null
    var spinner_position = Variables.meal_name

    @SuppressLint("ResourceType")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.dialog_fragment, container, false)
        var dialog_product_name: TextView = rootView.findViewById(R.id.dialog_product_name)
        var dialog_amount: EditText = rootView.findViewById(R.id.dialog_amount)
        var dialog_calory: TextView = rootView.findViewById(R.id.dialog_calory)
        var dialog_fats: TextView = rootView.findViewById(R.id.dialog_fats)
        var dialog_carbohydrates: TextView = rootView.findViewById(R.id.dialog_carbohydrates)
        var dialog_proteins: TextView = rootView.findViewById(R.id.dialog_proteins)
        var add_btn: ImageButton = rootView.findViewById(R.id.dialog_add_button)
        var add_favorite_btn: ImageButton = rootView.findViewById(R.id.dialog_favorite_add_button)
        var meal_choice: Spinner = rootView.findViewById(R.id.meal_choice)
        if(Variables.fav_or_not){
            add_favorite_btn.visibility = View.GONE
        }else{
            add_favorite_btn.visibility = View.VISIBLE
        }
        dialog_amount.setText("100")
        if(name_values != null){
            dialog_product_name.text = name_values
        }
        var amount = 0
        if(dialog_amount.text.toString() != ""){
            amount = dialog_amount.text.toString().toInt()
        }
            var api = retrofit.create(ApiService::class.java)
            var call = api.getInfoAboutProduct(id_values!!,amount)
        informationAboutProductsFromAPI(call, dialog_proteins, dialog_calory, dialog_fats, dialog_carbohydrates)
        dialog_amount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calory_amount =  "0.0"
                proteins_amount = "0.0"
                fats_amount = "0.0"
                carbohydrates_amount = "0.0"
                if(dialog_amount.text.toString() != ""){
                    amount = dialog_amount.text.toString().toInt()
                    var api = retrofit.create(ApiService::class.java)
                    var call = api.getInfoAboutProduct(id_values!!,amount)
                    informationAboutProductsFromAPI(call, dialog_proteins, dialog_calory, dialog_fats, dialog_carbohydrates)
                }else{
                    dialog_proteins.text = "Proteins: $proteins_amount"
                    dialog_calory.text = "Calories: $calory_amount"
                    dialog_fats.text = "Fats: $fats_amount"
                    dialog_carbohydrates.text = "Carbohydrates $carbohydrates_amount"
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        realm.executeTransaction {
                val history_val = it.createObject(history_value::class.java, ObjectId())
                history_val.owner_id = Variables.app?.currentUser()?.id
                history_val.product_id = id_values?.toLong()
                history_val.amount = dialog_amount.text.toString().toDouble()
                history_val.name = dialog_product_name.text.toString()
                history_val.date = search_date
                Log.v("profile", "Successfully insert data in history")
        }
        var actual_date = Variables.choosen_date
        var parse_date = Date.from(actual_date.atStartOfDay(ZoneId.systemDefault()).toInstant())
        when{
            Variables.meal_name == "breakfast" -> {
                meal_choice.setSelection(0)
            }
            Variables.meal_name == "lunchtime" -> {
                meal_choice.setSelection(1)
            }
            Variables.meal_name == "snacks" -> {
                meal_choice.setSelection(2)
            }
            Variables.meal_name == "dinner" -> {
                meal_choice.setSelection(3)
            }
        }

        add_btn.setOnClickListener{
            realm.executeTransaction {
                val dataFromFood = it.where<days_value>().equalTo("date", parse_date).findFirst()
                when {
                    meal_choice.selectedItemPosition == 0 -> {
                        dataFromFood?.breakfast?.forEach {
                            if(it.name == name_values.toString()){
                                it.amount = it.amount?.plus(dialog_amount.text.toString().toLong())
                                it.calories = it.calories?.plus(calory_amount?.toDouble()!!)
                                it.carbohydrates = it.carbohydrates?.plus(carbohydrates_amount?.toDouble()!!)
                                it.fats = it.fats?.plus(fats_amount?.toDouble()!!)
                                it.proteins = it.proteins?.plus(proteins_amount?.toDouble()!!)
                            }
                        }
                        if(dataFromFood?.breakfast?.find { it.name == name_values.toString()} == null){
                            dataFromFood?.breakfast?.add(
                                    days_value_breakfast(
                                            dialog_amount.text.toString().toLong(),
                                            calory_amount?.toDouble(),
                                            carbohydrates_amount?.toDouble(),
                                            fats_amount?.toDouble(),
                                            id_values?.toLong(),
                                            name_values.toString(),
                                            proteins_amount?.toDouble()))
                        }
                        dismiss()
                        Toast.makeText(getContext(),"Product has been added to breakfast", Toast.LENGTH_SHORT).show()
                    }
                    meal_choice.selectedItemPosition == 1 -> {
                        dataFromFood?.lunchtime?.forEach {
                            if(it.name == name_values.toString()){
                                it.amount = it.amount?.plus(dialog_amount.text.toString().toLong())
                                it.calories = it.calories?.plus(calory_amount?.toDouble()!!)
                                it.carbohydrates = it.carbohydrates?.plus(carbohydrates_amount?.toDouble()!!)
                                it.fats = it.fats?.plus(fats_amount?.toDouble()!!)
                                it.proteins = it.proteins?.plus(proteins_amount?.toDouble()!!)
                            }
                        }
                        if(dataFromFood?.lunchtime?.find { it.name == name_values.toString()} == null){
                            dataFromFood?.lunchtime?.add(
                                    days_value_lunchtime(
                                            dialog_amount.text.toString().toLong(),
                                            calory_amount?.toDouble(),
                                            carbohydrates_amount?.toDouble(),
                                            fats_amount?.toDouble(),
                                            id_values?.toLong(),
                                            name_values.toString(),
                                            proteins_amount?.toDouble()))
                        }
                        dismiss()
                        Toast.makeText(getContext(),"Product has been added to lunchtime", Toast.LENGTH_SHORT).show()
                    }
                    meal_choice.selectedItemPosition == 2 -> {
                        dataFromFood?.snacks?.forEach {
                            if(it.name == name_values.toString()){
                                it.amount = it.amount?.plus(dialog_amount.text.toString().toLong())
                                it.calories = it.calories?.plus(calory_amount?.toDouble()!!)
                                it.carbohydrates = it.carbohydrates?.plus(carbohydrates_amount?.toDouble()!!)
                                it.fats = it.fats?.plus(fats_amount?.toDouble()!!)
                                it.proteins = it.proteins?.plus(proteins_amount?.toDouble()!!)
                            }
                        }
                        if(dataFromFood?.snacks?.find { it.name == name_values.toString()} == null){
                            dataFromFood?.snacks?.add(
                                    days_value_snacks(
                                            dialog_amount.text.toString().toLong(),
                                            calory_amount?.toDouble(),
                                            carbohydrates_amount?.toDouble(),
                                            fats_amount?.toDouble(),
                                            id_values?.toLong(),
                                            name_values.toString(),
                                            proteins_amount?.toDouble()))
                        }
                        dismiss()
                        Toast.makeText(getContext(),"Product has been added to snacks", Toast.LENGTH_SHORT).show()
                    }
                    meal_choice.selectedItemPosition == 3 -> {
                        dataFromFood?.dinner?.forEach {
                            if(it.name == name_values.toString()){
                                it.amount = it.amount?.plus(dialog_amount.text.toString().toLong())
                                it.calories = it.calories?.plus(calory_amount?.toDouble()!!)
                                it.carbohydrates = it.carbohydrates?.plus(carbohydrates_amount?.toDouble()!!)
                                it.fats = it.fats?.plus(fats_amount?.toDouble()!!)
                                it.proteins = it.proteins?.plus(proteins_amount?.toDouble()!!)
                            }
                        }
                        if(dataFromFood?.dinner?.find { it.name == name_values.toString()} == null){
                            dataFromFood?.dinner?.add(
                                    days_value_dinner(
                                            dialog_amount.text.toString().toLong(),
                                            calory_amount?.toDouble(),
                                            carbohydrates_amount?.toDouble(),
                                            fats_amount?.toDouble(),
                                            id_values?.toLong(),
                                            name_values.toString(),
                                            proteins_amount?.toDouble()))
                        }
                        dismiss()
                        Toast.makeText(getContext(),"Product has been added to dinner", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
        add_favorite_btn.setOnClickListener {
            var dontadd:Boolean = false
            realm.executeTransaction {
                val favorite_products = it.where<favorite_list_value>().findAll()
                for(prod in favorite_products) {
                    if (prod.name == dialog_product_name.text.toString()) {
                        dontadd = true
                        dismiss()
                        Toast.makeText(getContext(),"This product is already on favorite product list", Toast.LENGTH_SHORT).show()
                    }
                }
                if(!dontadd){
                    val favorite_products_val = it.createObject(favorite_list_value::class.java, ObjectId())
                    favorite_products_val.owner_id = Variables.app?.currentUser()?.id
                    favorite_products_val.product_id = id_values?.toLong()
                    favorite_products_val.amount = dialog_amount.text.toString().toDouble()
                    favorite_products_val.name = dialog_product_name.text.toString()
                    Log.v("profile", "Successfully insert data in favorite products")
                    dismiss()
                    Toast.makeText(getContext(),"Product add to favorite product list", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return rootView
    }

    private fun informationAboutProductsFromAPI(call: Call<Nutriens_Info>, dialog_proteins: TextView, dialog_calory: TextView, dialog_fats: TextView, dialog_carbohydrates: TextView) {
        Handler(Looper.getMainLooper()).post {
            call?.enqueue(object : Callback<Nutriens_Info> {
                override fun onResponse(call: Call<Nutriens_Info>, response: Response<Nutriens_Info>) {
                    Log.v("Suc", "Success ")
                    var templist = response.body()?.nutrition?.nutrients
                    var calory_index = templist?.indexOf(templist.find { it.name == "Calories" })
                    var protein_index = templist?.indexOf(templist.find { it.name == "Protein" })
                    var fats_index = templist?.indexOf(templist.find { it.name == "Fat" })
                    var carbohydrates_index = templist?.indexOf(templist.find { it.name == "Carbohydrates" })
                    calory_amount = response.body()?.nutrition?.nutrients?.get(calory_index!!)!!.amount.toString()
                    proteins_amount = response.body()?.nutrition?.nutrients?.get(protein_index!!)!!.amount.toString()
                    fats_amount = response.body()?.nutrition?.nutrients?.get(fats_index!!)!!.amount.toString()
                    carbohydrates_amount = response.body()?.nutrition?.nutrients?.get(carbohydrates_index!!)!!.amount.toString()
                    dialog_proteins.text = "Proteins: $proteins_amount"
                    dialog_calory.text = "Calories: $calory_amount"
                    dialog_fats.text = "Fats: $fats_amount"
                    dialog_carbohydrates.text = "Carbohydrates $carbohydrates_amount"
                }

                override fun onFailure(call: Call<Nutriens_Info>, t: Throwable) {
                    Log.v("Suc", t.message.toString())
                }
            })
        }
    }

    fun get_Values(id:Int, name:String, date: Date){
        id_values = id
        name_values = name
        search_date = date
    }
}

