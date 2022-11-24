package com.miku.mubb.viewmodel

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.miku.mubb.R
import com.miku.mubb.bean.WebPageInfo
import com.miku.mubb.utils.ContextProvider
import com.miku.mubb.utils.LogRecorder

class BrowserModel : ViewModel() {
    private val history = MutableLiveData<MutableList<WebPageInfo>>()
    @SuppressLint("StaticFieldLeak")
    private lateinit var webView: WebView
    val mDefUrl = "https://www.google.co.jp"
    var shouldClearGoBackHistory = false
    var curWebPageInfo = MutableLiveData<WebPageInfo>()

    fun saveHistory(webPageInfo: WebPageInfo) {
        history.value?.add(webPageInfo)
    }

    fun getCurUrl(): String? {
        curWebPageInfo.value?.url?.apply {
            return this
        }
        return null
    }

    fun getCurTitle(): String {
        curWebPageInfo.value?.title?.apply {
            return this
        }
        return ContextProvider.getString(R.string.webpage_non_title)
    }

    fun setupWebView(webView: WebView) {
        this.webView = webView
    }

    fun loadUrl(webPageUrl: String) {
        webView.loadUrl(webPageUrl)
    }

    fun goBack(): Boolean {
        webView.apply {
            return if (canGoBack()) {
                goBack()
                true
            } else {
                false
            }
        }
    }

    fun goForward(): Boolean {
        webView.apply {
            return if (canGoForward()) {
                goForward()
                true
            } else {
                false
            }
        }
    }

    fun returnHomePage() {
        loadUrl(mDefUrl)
        shouldClearGoBackHistory = true
    }

    fun clearGoBackHistory() {
        webView.clearHistory()
        LogRecorder.d(TAG, "Back history clear.")
        shouldClearGoBackHistory = false
    }

    companion object {
        const val TAG = "BrowserModel"
    }
}
