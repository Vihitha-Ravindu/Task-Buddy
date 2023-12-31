package com.example.taskbuddy.Search

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskbuddy.Adaptors.PlumberAdaptor
import com.example.taskbuddy.Modals.ServiceProviderModal
import com.example.taskbuddy.Modals.UserModal
import com.example.taskbuddy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PlumberSearch : AppCompatActivity() {
    private lateinit var plumberResult: ArrayList<ServiceProviderModal>
    private lateinit var dbRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var plumberRecyclerView: RecyclerView
    private lateinit var plumberAdapter: PlumberAdaptor
    private var ulocation: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plumber_search)

        plumberResult = ArrayList()

        plumberRecyclerView = findViewById(R.id.plumberrecyclerview)
        plumberRecyclerView.layoutManager = LinearLayoutManager(this)
        plumberAdapter = PlumberAdaptor(this, plumberResult)
        plumberRecyclerView.adapter = plumberAdapter

        firebaseAuth = FirebaseAuth.getInstance()
        getuserlocation()
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
                        if (userresults != null) {
                            ulocation = userresults.location
                            getserviceproviders()
                        } else {
                            Log.e("PlumberSearch", "User location not found in database")
                        }
                    } else {
                        Log.e("PlumberSearch", "User data does not exist in the database")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("PlumberSearch", "Database error: ${error.message}")
                    Toast.makeText(this@PlumberSearch, "There is a problem retrieving data from the database", Toast.LENGTH_LONG).show()
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

                    plumberResult.sortByDescending { it.rating }
                    plumberAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("PlumberSearch", "Database error: ${error.message}")
                    Toast.makeText(this@PlumberSearch, "There is a problem retrieving data from the database", Toast.LENGTH_LONG).show()
                }
            })
    }
}
