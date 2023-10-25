package com.example.taskbuddy.WelcomePage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.taskbuddy.MainActivity
import com.example.taskbuddy.R
import com.example.taskbuddy.ServiceProviderFragment.ServiceProviderHome
import com.google.firebase.auth.FirebaseAuth

class ServiceproviderSignInPage : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private  lateinit var signinbtn: Button
    private lateinit var fogotpwdbtn: TextView
    private lateinit var newacc: TextView
    private lateinit var passwd: EditText
    private lateinit var uemail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serviceprovider_sign_in_page)
        firebaseAuth = FirebaseAuth.getInstance()

        signinbtn = findViewById(R.id.log_button)
        fogotpwdbtn = findViewById(R.id.forgetPwd)
        newacc = findViewById(R.id.noAcc)
        uemail = findViewById(R.id.log_email)
        passwd = findViewById(R.id.log_password)

        fogotpwdbtn.setOnClickListener{
            val intent = Intent(this,ResetPasswordPage::class.java)
            startActivity(intent)
        }

        newacc.setOnClickListener{
            val intent = Intent(this,SignUpPage::class.java)
            startActivity(intent)
        }

        signinbtn.setOnClickListener{
            val email = uemail.text.toString()
            val pass = passwd.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, ServiceProviderHome::class.java)
                        startActivity(intent)

                        val user = firebaseAuth.currentUser
                        user?.let {

                            val name = it.displayName
                            val email = it.email
                            val photoUrl = it.photoUrl

                            val emailVerified = it.isEmailVerified

                            val uid = it.uid

                            println(name)
                            println(email)
                            println(photoUrl)
                            println(emailVerified)
                            println(uid)
                        }

                    } else {
                        Toast.makeText(this, "Email or Password is Incorrect", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}