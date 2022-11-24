package com.miku.mubb.webview

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.* // ktlint-disable no-wildcard-imports
import androidx.preference.PreferenceManager
import com.miku.mubb.MainActivity
import com.miku.mubb.R
import com.miku.mubb.bean.WebPageInfo
import com.miku.mubb.settings.ENABLE_JAVASCRIPT
import com.miku.mubb.settings.ENABLE_ZOOM
import com.miku.mubb.utils.ContextProvider

class WebViewClientMiku constructor(private val activity: MainActivity) : WebViewClient() {
    var curTitle = ContextProvider.getString(R.string.webpage_non_title)

    /**
     * 为true时，说明页面已经成功加载，否则说明加载失败，显示错误页
     * @loadStatus will be set to true if a page loaded successfully,
     * otherwise it will be set to false.
     */
    private var loadStatus = true

    private val webChromeMiku = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            view?.title?.apply {
                curTitle = this
            }
            activity.getBrowserModel().curWebPageInfo.value = WebPageInfo(curTitle, view?.url.toString())
        }
    }

    fun initSetup(webView: WebView) {
        val enableZoom = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(
            ENABLE_ZOOM, false
        )
        val enableJs = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(
            ENABLE_JAVASCRIPT, false
        )
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.javaScriptEnabled = enableJs
        // webView.settings.builtInZoomControls = true
        webView.settings.setSupportZoom(enableZoom)
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
        if (loadStatus) {
            activity.hideTipView()
        } else {
            activity.apply {
                showTipView(getString(R.string.tip_error), R.mipmap.miku_error)
            }
        }
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        loadStatus = false
        super.onReceivedError(view, request, error)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        activity.apply {
            showTipView(getString(R.string.tip_loading), R.mipmap.miku_loading)
        }
        loadStatus = true
        super.onPageStarted(view, url, favicon)
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        super.onLoadResource(view, url)
        activity.apply {
            showTipView(getString(R.string.tip_requesting), R.mipmap.miku_requesting)
        }
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        /**
         * 在开始加载页面前，这应该被设置为false
         * @loadStatus should be set to false before a page is loaded
         */
        loadStatus = false
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
