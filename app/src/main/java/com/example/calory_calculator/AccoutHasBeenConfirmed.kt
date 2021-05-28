package com.example.calory_calculator

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class AccoutHasBeenConfirmed : AppCompatActivity() {
    var uri: Uri?= null
    var token:String? = null
    var tokenid:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accout_has_been_confirmed)
        var confirmed_accout = findViewById<TextView>(R.id.confirmed_acc_text)
        var gotologin = findViewById<Button>(R.id.gotologin)
        intent = getIntent()
        uri = intent.data
        token = uri?.getQueryParameter("token")
        tokenid = uri?.getQueryParameter("tokenId")
        Variables.app?.emailPassword?.confirmUserAsync(token,tokenid){
            if(it.isSuccess){
                confirmed_accout.text = "Accout has been confirmed"
            }else{
                confirmed_accout.text = "Accout cannot be confirmed. Try again later"
            }
        }
        gotologin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}