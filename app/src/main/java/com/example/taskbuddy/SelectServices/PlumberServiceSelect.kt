package com.example.taskbuddy.SelectServices

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.example.taskbuddy.Adapter.CreditCard
import com.example.taskbuddy.Modals.orderdetails
import com.example.taskbuddy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class PlumberServiceSelect : AppCompatActivity() {
    private var serviceprovider: String? = null
    private  var slocation: String? = null
    private  var srating: Int = 0
    private var totalpayment: Double = 0.0
    private  var snumber: String? = null
    private lateinit var emergencyrepaires: CheckBox
    private lateinit var leakrepaire: CheckBox
    private lateinit var pipeinstallation: CheckBox
    private lateinit var draincleaning: CheckBox
    private lateinit var barthroomplumbing: CheckBox
    private lateinit var kitachenplumbing: CheckBox
    private lateinit var proccedbtn: Button
    private lateinit var sname: TextView
    private lateinit var location: TextView
    private lateinit var phonenumber: TextView
    private lateinit var rating: TextView
    private lateinit var payableamount:TextView
    private lateinit var dbref: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var orderhistory: ArrayList<String>
    private lateinit var time: String
    private var servicepId: String? = null
    private var emergencycost: Double = 1000.0
    private var leakcost: Double = 800.0
    private var pipecost: Double = 1000.0
    private var draincost: Double = 500.0
    private var barthroomcost: Double = 1100.0
    private var kitachencost: Double = 1300.0
    private var totalAmount: Double = 0.0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plumber_service_select)

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        time = "$hour.$minute"

        orderhistory = ArrayList()

        emergencyrepaires = findViewById(R.id.emergencyrepaire)
        leakrepaire = findViewById(R.id.leakrepaire)
        pipeinstallation = findViewById(R.id.pipeinstallation)
        draincleaning = findViewById(R.id.driancleaning)
        barthroomplumbing = findViewById(R.id.barthroomplumb)
        kitachenplumbing = findViewById(R.id.kitchenplumb)
        proccedbtn = findViewById(R.id.proceedbtn)
        sname = findViewById(R.id.serviceprovidername)
        location = findViewById(R.id.serviceproviderlocation)
        phonenumber = findViewById(R.id.serviceprovidernumber)
        rating = findViewById(R.id.serviceproviderrating)
        payableamount = findViewById(R.id.payableamount)

        serviceprovider = intent.getStringExtra("name")
        slocation = intent.getStringExtra("location")
        snumber = intent.getStringExtra("number")
        srating = intent.getIntExtra("rating",0)
        servicepId = intent.getStringExtra("id")

        sname.text = serviceprovider
        location.text = slocation
        phonenumber.text = snumber
        rating.text = srating.toString()

        emergencyrepaires.text = "Emergency Repairs ( $emergencycost)"
        leakrepaire.text = "Leak Repairs ($leakcost)"
        pipeinstallation.text = "Pipe Installation ($pipecost)"
        draincleaning.text = "Drain Cleaning ($draincost)"
        barthroomplumbing.text = "Bathroom Plumbing ($barthroomcost)"
        kitachenplumbing.text = "Kitchen Plumbing ($kitachencost)"

        firebaseAuth = FirebaseAuth.getInstance()

        totalpayment = calculateTotalAmount(emergencyrepaires.isChecked,leakrepaire.isChecked,pipeinstallation.isChecked,draincleaning.isChecked,barthroomplumbing.isChecked,kitachenplumbing.isChecked)

        payableamount.text = totalpayment.toString()

        proccedbtn.setOnClickListener{
            setorderdetails()
            val intent = Intent(this,CreditCard::class.java)
            startActivity(intent)
        }
    }

    private fun calculateTotalAmount(emergency: Boolean, leak: Boolean,pipe: Boolean,drain: Boolean,barthroom: Boolean,kitachen: Boolean): Double {


        if (emergency) {
            totalAmount += emergencycost
            orderhistory.add("emergency repaire")
        }

        if (leak) {
            totalAmount += leakcost
            orderhistory.add("leak repaire")

        }

        if (pipe) {
            totalAmount += pipecost
            orderhistory.add("pipe fixing")

        }
        if (drain) {
            totalAmount += draincost
            orderhistory.add("drain repaire")

        }
        if (barthroom) {
            totalAmount += barthroomcost
            orderhistory.add("barthroom plumbing")

        }
        if (kitachen) {
            totalAmount += kitachencost
            orderhistory.add("kitchen plumbing")

        }

        return totalAmount
    }

    private fun setorderdetails(){
        val user = firebaseAuth.currentUser
        val userid = user!!.uid
        var orderid = ""
        dbref = FirebaseDatabase.getInstance().getReference("orderdetails")

        orderid = dbref.push().key!!

        val order = orderdetails(userid,orderhistory,time,serviceprovider)

        dbref.child(orderid).setValue(order)
            .addOnCompleteListener {
                Toast.makeText(this, "order added Successfully", Toast.LENGTH_LONG).show()
            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
            }
    }

}