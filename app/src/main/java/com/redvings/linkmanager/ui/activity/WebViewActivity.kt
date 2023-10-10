package com.redvings.linkmanager.ui.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.redvings.linkmanager.base.BaseActivity
import com.redvings.linkmanager.databinding.ActivityWebviewBinding
import com.redvings.linkmanager.models.LinkModel
import com.redvings.linkmanager.utils.Utils.Commons
import com.redvings.linkmanager.utils.Utils.eLog
import com.redvings.linkmanager.utils.Utils.getSerializableData

class WebViewActivity : BaseActivity() {
    private lateinit var binding: ActivityWebviewBinding

    private val linkModel by lazy {
        intent.getSerializableData<LinkModel>(Commons.DATA_BUNDLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    private fun setup() {
        binding.toolbar.tvTitle.text = linkModel?.name
        loadUrl(linkModel?.link)
        eLog("loadURL ::: ${linkModel?.link}")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadUrl(link: String?) {
        if (link == null) return
        with(binding) {
            webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String, f: Bitmap?) {
                    showLoader(true)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    showLoader(false)
                }
            }
            webView.settings.javaScriptEnabled = true
            webView.settings.userAgentString = "Android WebView"
            webView.settings.setSupportZoom(true)
            showLoader(true)
            webView.loadUrl(link)
        }
    }
}