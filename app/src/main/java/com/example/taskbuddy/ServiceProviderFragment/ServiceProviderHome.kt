package com.example.taskbuddy.ServiceProviderFragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.taskbuddy.R


class ServiceProviderHome : AppCompatActivity() {
    private lateinit var ser1 : ImageButton
    private lateinit var ser2: ImageButton
    private lateinit var ser3: ImageButton
    private lateinit var ser4: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_provider_home)

        ser1 = findViewById(R.id.imageButton)
        ser2 = findViewById(R.id.imageButton5)
        ser3 = findViewById(R.id.imageButton6)
        ser4 = findViewById(R.id.imageButton7)

        val ser1Frag = ServiceHomeFragment()
        val ser2Frag = ServiceUserFragment()
        val ser3Frag = ServiceHistoryFragment()
        val ser4Frag = ServiceOngoingFragment()

        ser1.setOnClickListener{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView2, ser1Frag)
                commit()
            }
        }

        ser2.setOnClickListener{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView2, ser2Frag)
                commit()
            }
        }

        ser3.setOnClickListener{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView2, ser3Frag)
                commit()
            }
        }

        ser4.setOnClickListener{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView2, ser4Frag)
                commit()
            }
        }
    }
}