package com.example.calory_calculator

import android.app.Application
import android.util.Log
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncSession
import java.util.*

lateinit var app: App
var user: User? = null
var realm: Realm? = null

 class MyApplication : Application(){
    private val app_Id: String = "calorie-calculator-svwsz"

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val handler =
            SyncSession.ClientResetHandler { session, error ->
                Log.e(
                    "EXAMPLE",
                    "Client Reset required for: ${session.configuration.serverUrl} for error: $error"
                )
            }

        app = App(
            AppConfiguration.Builder(app_Id)
                .defaultClientResetHandler(handler)
                .build()
        )

        val tzone = TimeZone.getTimeZone("Europe/Warsaw")
        TimeZone.setDefault(tzone)
    }


}

