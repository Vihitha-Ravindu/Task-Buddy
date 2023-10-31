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
    private  var srating: Float = 0F
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
    private var orderhistory: ArrayList<String> = ArrayList()
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

        orderhistory = ArrayList()

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
        srating = intent.getFloatExtra("rating",0.0F)
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

        emergencyrepaires.setOnCheckedChangeListener { buttonView, isChecked ->
            calculateTotalAmount()
            updatePayableAmount()
        }

        leakrepaire.setOnCheckedChangeListener { buttonView, isChecked ->
            calculateTotalAmount()
            updatePayableAmount()
        }

        pipeinstallation.setOnCheckedChangeListener { buttonView, isChecked ->
            calculateTotalAmount()
            updatePayableAmount()
        }

        draincleaning.setOnCheckedChangeListener { buttonView, isChecked ->
            calculateTotalAmount()
            updatePayableAmount()
        }

        barthroomplumbing.setOnCheckedChangeListener { buttonView, isChecked ->
            calculateTotalAmount()
            updatePayableAmount()
        }

        kitachenplumbing.setOnCheckedChangeListener { buttonView, isChecked ->
            calculateTotalAmount()
            updatePayableAmount()
        }

        firebaseAuth = FirebaseAuth.getInstance()


        proccedbtn.setOnClickListener {
            setorderdetails()
            val cardDetailsFragment = CreditCard()
            cardDetailsFragment.show(
                supportFragmentManager,
                cardDetailsFragment.tag
            )
        }

    }

    private fun calculateTotalAmount() {
        totalAmount = 0.0
        orderhistory.clear()

        if (emergencyrepaires.isChecked) {
            totalAmount += emergencycost
            orderhistory.add("emergency repaire")
        }

        if (leakrepaire.isChecked) {
            totalAmount += leakcost
            orderhistory.add("leak repaire")
        }

        if (pipeinstallation.isChecked) {
            totalAmount += pipecost
            orderhistory.add("pipe fixing")
        }

        if (draincleaning.isChecked) {
            totalAmount += draincost
            orderhistory.add("drain repaire")
        }

        if (barthroomplumbing.isChecked) {
            totalAmount += barthroomcost
            orderhistory.add("barthroom plumbing")
        }

        if (kitachenplumbing.isChecked) {
            totalAmount += kitachencost
            orderhistory.add("kitchen plumbing")
        }
    }

    private fun updatePayableAmount() {
        payableamount.text = totalAmount.toString()
    }


    private fun setorderdetails() {
        val user = firebaseAuth.currentUser
        val userid = user!!.uid
        var orderid = ""
        dbref = FirebaseDatabase.getInstance().getReference("orderdetails")

        orderid = dbref.push().key!!
        val servicesUsed = orderhistory.toList()


        val order = orderdetails(orderid,userid,servicesUsed,time,servicepId,totalAmount,"yes")


        dbref.child(orderid).setValue(order)
            .addOnSuccessListener {
                Toast.makeText(this, "Order added Successfully", Toast.LENGTH_LONG).show()
            }


            .addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
            }
    }


}