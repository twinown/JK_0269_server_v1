package com.template

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState!=null){
            openLoadingActivity()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
    }
    private fun openLoadingActivity() {
        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)
    }

}
