package com.example.taskbuddy.WelcomePage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.taskbuddy.R

class WelcomePage : AppCompatActivity() {
    private lateinit var signin: Button
    private lateinit var  signup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)


        signin = findViewById(R.id.WelcomePage_signinbtn)
        signup = findViewById(R.id.welcomepage_signupbtn)

        signin.setOnClickListener{
            val intent = Intent(this,SignInPage::class.java)
            startActivity(intent)
        }

        signup.setOnClickListener{
            val intent = Intent(this,SignUpPage::class.java)
            startActivity(intent)
        }
    }
}