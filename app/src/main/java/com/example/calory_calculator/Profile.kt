package com.example.calory_calculator

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.calory_calculator.MODELS.calory_value
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.sync.SyncConfiguration
import org.bson.types.ObjectId

class Profile : AppCompatActivity() {
/*        val user = app.currentUser()
        val config = SyncConfiguration
            .Builder(user, app.currentUser()?.id)
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
        var realm : Realm = Realm.getInstance(config)*/
        var gender_value: String? = null
        var growth_value: String? = null
        var weight_value: String? = null
        var age_value: String? = null
        var physical_activity_value: String? = null
        var destination_value: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)
        if(!Statistics.isWifiConnected(applicationContext)){
            val intent = Intent(this, NoNetworkConnection::class.java)
            startActivity(intent)
        }
        Variables.clear_or_not = false
        if(!realm?.isAutoRefresh!!){
            realm?.refresh()
        }
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        var preferences = PreferenceManager.getDefaultSharedPreferences(this)
        var editor = preferences?.edit()

            realm?.executeTransaction {
                val dataFromProfile = it.where<calory_value>().findFirst()
                if(dataFromProfile != null){
                    editor?.putString("gender", dataFromProfile?.gender)
                    editor?.putString("growth", dataFromProfile?.growth.toString())
                    editor?.putString("weight", dataFromProfile?.weight.toString())
                    editor?.putString("age", dataFromProfile?.age.toString())
                    editor?.putString("physical_activity", dataFromProfile?.physical_activity)
                    editor?.putString("destination", dataFromProfile?.destination)
                    editor?.apply()
                    Log.v("profile", "Successfully get data from realm")
                }else{
                    Log.v("profile", "No data in realm")
                }
            }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        Variables.clear_or_not = true
        realm?.close()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                var preferences = PreferenceManager.getDefaultSharedPreferences(this)
                gender_value = preferences?.getString("gender", null)
                growth_value = preferences?.getString("growth", null)
                weight_value = preferences?.getString("weight", null)
                age_value = preferences?.getString("age", null)
                physical_activity_value = preferences?.getString("physical_activity", null)
                destination_value = preferences?.getString("destination", null)

                if (!gender_value.isNullOrBlank() && !growth_value.isNullOrBlank() && !weight_value.isNullOrBlank()
                        && !age_value.isNullOrBlank() && !physical_activity_value.isNullOrBlank() && !destination_value.isNullOrBlank() && growth_value?.toInt() != 0 && weight_value?.toInt() != 0 && age_value?.toInt() != 0){
                    Handler(Looper.getMainLooper()).post {
                        realm?.executeTransaction {
                            val dataFromProfile = it.where<calory_value>().findFirst()
                            if (dataFromProfile != null) {
                                dataFromProfile.age = age_value?.toLong()
                                dataFromProfile.destination = destination_value
                                dataFromProfile.gender = gender_value
                                dataFromProfile.growth = growth_value?.toLong()
                                dataFromProfile.owner_id = app.currentUser()?.id
                                dataFromProfile.physical_activity = physical_activity_value
                                dataFromProfile.weight = weight_value?.toLong()
                                Log.v("profile", "Successfully update data in realm")
                            } else {
                                val calory_val = it.createObject(calory_value::class.java, ObjectId())
                                calory_val.age = age_value?.toLong()
                                calory_val.destination = destination_value
                                calory_val.gender = gender_value
                                calory_val.growth = growth_value?.toLong()
                                calory_val.owner_id = app.currentUser()?.id
                                calory_val.physical_activity = physical_activity_value
                                calory_val.weight = weight_value?.toLong()
                                Log.v("profile", "Successfully insert data in realm")
                            }
                        }
                    }
                }else{
                    Log.v("profile", "Insert values in all fields")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}