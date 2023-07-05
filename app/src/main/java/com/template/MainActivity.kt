package com.template

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("nn97", "main act in create "+ savedInstanceState)

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
    }


}
