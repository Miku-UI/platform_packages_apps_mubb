package com.miku.mubb.webview

import android.content.Intent
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.miku.mubb.MainActivity
import com.miku.mubb.R
import com.miku.mubb.bean.WebPageInfo
import com.miku.mubb.utils.ContextProvider

class WebViewClientMiku constructor(private val activity: MainActivity) : WebViewClient() {
    var curTitle = ContextProvider.getString(R.string.webpage_non_title)

    val webChromeMiku = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            view?.title?.apply {
                curTitle = this
            }
            activity.getBrowserModel().curWebPageInfo.value = WebPageInfo(curTitle, view?.url.toString())
        }
    }

    fun initSetup(webView: WebView) {
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.javaScriptEnabled = true
        // webView.settings.builtInZoomControls = true
        webView.settings.setSupportZoom(true)
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.webChromeClient = webChromeMiku
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        activity.getBrowserModel().apply {
            if (this.shouldClearGoBackHistory) {
                this.clearGoBackHistory()
            }
        }
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (request?.url == null) return false
        try {
            return if (request.url.toString().startsWith("http://") ||
                request.url.toString().startsWith("https://")
            ) {
                // 处理http和https开头的url
                // Handle url starts with 'http' or 'https'
                // view?.loadUrl(request.url.toString())
                activity.getBrowserModel().loadUrl(request.url.toString())
                true
            } else {
                // 其他自定义的scheme
                // Other custom scheme
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(request.url.toString()))
                activity.startActivity(intent)
                true
            }
        } catch (e: Exception) {
            // 防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
            // Prevent crash
            // 没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
            // If the target app is not installed, return true so we can get rid of Error Page
            return true
        }
    }
}
