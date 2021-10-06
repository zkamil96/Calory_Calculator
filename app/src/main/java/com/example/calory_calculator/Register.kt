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
import android.widget.ProgressBar
import android.widget.Toast
import java.util.regex.Pattern

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        if(!Statistics.isWifiConnected(applicationContext)){
            val intent = Intent(this, NoNetworkConnection::class.java)
            startActivity(intent)
        }
        val password_pattern:Pattern = Pattern.compile("^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$")
        var good_email:String? = null
        var good_password:String? = null
        var hash_password:String? = null
        val email_value = findViewById<EditText>(R.id.email_value)
        val password_value = findViewById<EditText>(R.id.password_value)
        val repassword_value = findViewById<EditText>(R.id.repassword_value)
        val register_user = findViewById<Button>(R.id.register_user)
        val pb: ProgressBar =  findViewById(R.id.loadingProgressRegister);

        email_value.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (Patterns.EMAIL_ADDRESS.matcher(email_value.text.toString().trim())
                        .matches())
                {
                    good_email = email_value.text.toString()
                } else {
                    good_email = null
                    email_value.setError("Invalid Email")
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        password_value.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (password_pattern.matcher(password_value.text.toString().trim())
                        .matches())
                {
                    good_password = password_value.text.toString()
                } else {
                    good_password = null
                    password_value.setError("Password must have at least one number, one lower and one large letter and have at least 8 characters")
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        repassword_value.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (repassword_value.text.toString() == good_password)
                {
                    hash_password = good_password
                } else {
                    hash_password = null
                    repassword_value.setError("Password must be the same")
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        register_user.setOnClickListener(){
            if(good_email != null && hash_password != null){
                pb.visibility = View.VISIBLE
                register_user.visibility = View.INVISIBLE
                app.emailPassword?.registerUserAsync(good_email, hash_password){
                    if (it.isSuccess) {
                        Toast.makeText(this," Successful Registration", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ConfirmAccount::class.java)
                        intent.putExtra("email", good_email)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this,"User with that data already exist", Toast.LENGTH_SHORT).show()
                        pb.visibility = View.GONE
                        register_user.visibility = View.VISIBLE
                    }
                }
            }
            else{
                Toast.makeText(this,"Fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(app.currentUser() != null) {
            val intent = Intent(this, Statistics::class.java)
            startActivity(intent)
        }
    }
}