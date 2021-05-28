package com.example.calory_calculator

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.regex.Pattern

class Reset_password : AppCompatActivity() {
    var confirm_password_input:TextView? = null
    var confirm_password_input_confirm:TextView? = null
    var create_new_password:Button? = null
    var uri:Uri?= null
    var token:String? = null
    var tokenid:String? = null
    var good_password:String? = null
    var hash_password:String? = null
    val password_pattern: Pattern = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=\\S+$)" +           //no white spaces
            ".{8,}" +               //at least 8 characters
            "$")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        confirm_password_input = findViewById(R.id.reset_password_input)
        confirm_password_input_confirm = findViewById(R.id.reset_password_input_confirm)
        create_new_password = findViewById(R.id.new_password_button)
        intent = getIntent()
        uri = intent.data
        token = uri?.getQueryParameter("token")
        tokenid = uri?.getQueryParameter("tokenId")

        confirm_password_input?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (password_pattern.matcher(confirm_password_input?.text.toString().trim())
                                .matches())
                {
                    good_password = confirm_password_input?.text.toString()
                } else {
                    good_password = null
                    confirm_password_input?.setError("Password must have at least one number, one lower and one large letter and have at least 8 characters")
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        confirm_password_input_confirm?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (confirm_password_input_confirm?.text.toString() == good_password)
                {
                    hash_password = good_password
                } else {
                    hash_password = null
                    confirm_password_input_confirm?.setError("Password must be the same")
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        create_new_password?.setOnClickListener(){
            if(hash_password != null){
                Variables.app?.emailPassword?.resetPasswordAsync(token,tokenid,confirm_password_input?.text.toString()){
                    if (it.isSuccess) {
                        Toast.makeText(this,"Password has been changed. You can login now", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this,"Cannot reset password", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

        }
