package com.joinhub.complaintprotaluser

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.webkit.WebSettings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.joinhub.complaintprotaluser.databinding.ActivitySpeedTestBinding


class SpeedTestActivity : AppCompatActivity() {
    lateinit var binding:ActivitySpeedTestBinding
    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySpeedTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val webSettings: WebSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true;
        webSettings.useWideViewPort = true;
        webSettings.allowFileAccess = true;
        webSettings.allowContentAccess = true;
        webSettings.allowFileAccessFromFileURLs = true;
        webSettings.allowUniversalAccessFromFileURLs = true;
        webSettings.domStorageEnabled = true;
        binding.webView.loadUrl("https://fast.com/")

    }
}