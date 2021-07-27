package com.example.calory_calculator

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.sync.ClientResetRequiredError
import io.realm.mongodb.sync.Sync
import io.realm.mongodb.sync.SyncConfiguration
import io.realm.mongodb.sync.SyncSession
import java.util.*
import java.util.concurrent.ThreadLocalRandom.current


public class MyApplication : Application(){
    private val app_Id:String = "calorie-calculator-svwsz"

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
/*        val configuration = RealmConfiguration.Builder()
                .name("Calory_Calculator")
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build()

        Realm.setDefaultConfiguration(configuration)*/

        val handler =
            SyncSession.ClientResetHandler { session, error ->
                Log.e(
                    "EXAMPLE",
                    "Client Reset required for: ${session.configuration.serverUrl} for error: $error"
                )
            }
        Variables.app = App(
            AppConfiguration.Builder(app_Id)
                .defaultClientResetHandler(handler)
                .build()
        )

        val tzone = TimeZone.getTimeZone("Europe/Warsaw")
        TimeZone.setDefault(tzone)
    }


}

