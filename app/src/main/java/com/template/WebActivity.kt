package com.template

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat.startActivity

class WebActivity : AppCompatActivity() {


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
       // supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().flush()
        val sharedPref = this.getSharedPreferences("main",Context.MODE_PRIVATE) ?: return
        val url = sharedPref.getString("urlForWeb", "").toString()
        Log.d("nn97", "$url from sherpref")
        webView.loadUrl(url)
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val webView: WebView = findViewById(R.id.webView)
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

    }
}
