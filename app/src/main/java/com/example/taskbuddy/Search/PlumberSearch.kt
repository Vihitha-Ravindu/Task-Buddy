package com.example.taskbuddy.Search

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskbuddy.Adaptors.PlumberAdaptor
import com.example.taskbuddy.Modals.ServiceProviderModal
import com.example.taskbuddy.Modals.UserModal
import com.example.taskbuddy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PlumberSearch : AppCompatActivity() {
    private lateinit var plumberResult: ArrayList<ServiceProviderModal> // Initialize this property
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var plumberRecyclerView: RecyclerView
    private lateinit var plumberAdapter: PlumberAdaptor
    private var ulocation: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plumber_search)

        // Initialize plumberResult as an empty ArrayList
        plumberResult = ArrayList()

        plumberRecyclerView = findViewById(R.id.plumberrecyclerview)
        plumberRecyclerView.layoutManager = LinearLayoutManager(this)
        plumberAdapter = PlumberAdaptor(this, plumberResult)
        plumberRecyclerView.adapter = plumberAdapter

        firebaseAuth = FirebaseAuth.getInstance()

        getuserlocation()
        getserviceproviders()
    }

    private fun getuserlocation() {
        val user = firebaseAuth.currentUser
        var userid = ""
        user?.let {
            userid = it.uid

            dbRef = FirebaseDatabase.getInstance().getReference("users").child(userid)
            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userresults = snapshot.getValue(UserModal::class.java)
                        ulocation = userresults?.location
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@PlumberSearch, "There is a problem of retrieving data from the database", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun getserviceproviders() {
        dbRef = FirebaseDatabase.getInstance().getReference("serviceprovider")

        dbRef.orderByChild("service").equalTo("Plumber")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    plumberResult.clear()
                    for (snapshot in dataSnapshot.children) {
                        val item = snapshot.getValue(ServiceProviderModal::class.java)
                        item?.let {
                            if (ulocation == item.location) {
                                plumberResult.add(item)
                            }
                        }
                    }

                    // Sort the data list by the "rating" field from highest to lowest.
                    plumberResult.sortByDescending { it.rating }

                    plumberAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle any errors
                    Toast.makeText(this@PlumberSearch, "There is a problem of retrieving data from the database", Toast.LENGTH_LONG).show()
                }
            })
    }
}
