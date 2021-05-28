package com.example.calory_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class ConfirmAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_account)
        var confirm_input = findViewById<TextView>(R.id.confirm_account_input_text)
        var good_email = intent.getStringExtra("email")
        Variables.app?.emailPassword?.resendConfirmationEmailAsync(good_email){
            if(it.isSuccess){
                confirm_input.text = "Activation link has been send to your Email address."
            }else{
                Log.v("e", "Activation link cannot be send")
            }
        }
    }
}