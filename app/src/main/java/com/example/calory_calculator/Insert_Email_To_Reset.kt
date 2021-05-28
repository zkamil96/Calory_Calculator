package com.example.calory_calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Insert_Email_To_Reset : AppCompatActivity() {
    var reset_email_input: EditText? = null
    var send_reset_email: Button? = null
    var good_email:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert__email__to__reset)
        reset_email_input = findViewById(R.id.insert_email_to_reset_input)
        send_reset_email = findViewById(R.id.send_link_to_reset_button)
        reset_email_input?.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if (Patterns.EMAIL_ADDRESS.matcher(reset_email_input?.text.toString().trim())
                                        .matches())
                        {
                            good_email = reset_email_input?.text.toString()
                        } else {
                            good_email = null
                            reset_email_input?.setError("Invalid Email")
                        }
                    }
                    override fun afterTextChanged(s: Editable?) {
                    }
                })
        send_reset_email?.setOnClickListener(){
            if(good_email != null){
                Variables.app?.emailPassword?.sendResetPasswordEmailAsync(good_email){
                    if (it.isSuccess) {
                        Toast.makeText(this,"Reset Link has been send to your email", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this,"Wrong email", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Wrong email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}