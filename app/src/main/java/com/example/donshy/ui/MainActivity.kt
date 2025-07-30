package com.example.donshy.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.donshy.R
import com.example.donshy.ui.login.SecondFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Only add fragment if first launch
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SecondFragment())
                .commit()
        }
    }
}