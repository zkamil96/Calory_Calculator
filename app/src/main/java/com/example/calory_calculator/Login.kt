package com.example.calory_calculator

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import io.realm.mongodb.mongo.MongoClient
import io.realm.mongodb.mongo.MongoCollection
import io.realm.mongodb.mongo.MongoDatabase
import org.bson.Document
import java.util.regex.Pattern

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val password_pattern: Pattern = Pattern.compile("^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$")

        var good_email:String? = null
        var good_password:String? = null
        val email_input = findViewById<EditText>(R.id.email_input)
        val password_input = findViewById<EditText>(R.id.password_input)
        val login_user = findViewById<Button>(R.id.login_user)
        val forgot_password = findViewById<TextView>(R.id.password_forgot)
        var pb: ProgressBar =  findViewById(R.id.loadingProgress)

        forgot_password.setOnClickListener(){
            val intent = Intent(this, Insert_Email_To_Reset::class.java)
            startActivity(intent)
        }

        email_input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (Patterns.EMAIL_ADDRESS.matcher(email_input.text.toString().trim())
                        .matches())
                {
                    good_email = email_input.text.toString()
                } else {
                    good_email = null
                    email_input.setError("Invalid Email")
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        password_input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (password_pattern.matcher(password_input.text.toString().trim())
                        .matches())
                {
                    good_password = password_input.text.toString()
                } else {
                    good_password = null
                    password_input.setError("Password must have at least one number, one lower and one large letter and have at least 8 characters")
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        login_user.setOnClickListener(){
            if(good_email != null && good_password != null){
                pb.visibility = View.VISIBLE
                login_user.visibility = View.INVISIBLE
                val emailPasswordCredentials: Credentials = Credentials.emailPassword(
                    good_email,
                    good_password
                )
                Variables.app?.loginAsync(emailPasswordCredentials) {
                    if (it.isSuccess) {
                        val intent = Intent(this, Statistics::class.java)
                        startActivity(intent)
                    } else {
                        if(it.error.errorMessage.equals("confirmation required")){
                            val intent = Intent(this, ConfirmAccount::class.java)
                            intent.putExtra("email", good_email)
                            startActivity(intent)
                        }
                        Log.v("err", it.error.errorMessage.toString())
                        Toast.makeText(this,"Problem with login", Toast.LENGTH_SHORT).show()
                        pb.visibility = View.GONE
                        login_user.visibility = View.VISIBLE
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
        if(Variables?.app?.currentUser() != null) {
            val intent = Intent(this, Statistics::class.java)
            startActivity(intent)
        }
    }
}