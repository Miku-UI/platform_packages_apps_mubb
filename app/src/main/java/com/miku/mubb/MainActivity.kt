package com.miku.mubb

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.miku.mubb.viewmodel.BrowserModel
import com.miku.mubb.webview.WebViewClientMiku

class MainActivity : ComponentActivity() {
    // private val mBinding: MainPageBinding by lazy { MainPageBinding.inflate(layoutInflater) }
    private val mClient = WebViewClientMiku(this)
    private var mShouldGoBack = true
    private val mWebView: WebView by lazy { findViewById(R.id.webview) }
    private val mUrlTextEditor: TextInputEditText by lazy { findViewById(R.id.url_input) }
    private val mBrowserModel: BrowserModel by lazy { ViewModelProvider(this)[BrowserModel::class.java] }

    fun getBrowserModel(): BrowserModel {
        return mBrowserModel
    }

    private fun initBrowser() {
        mWebView.webViewClient = mClient
        mClient.initSetup(mWebView)
        mBrowserModel.setupWebView(mWebView)
        mUrlTextEditor.setOnFocusChangeListener { _, hasFocus ->
            mShouldGoBack = if (hasFocus) {
                mUrlTextEditor.setText(mBrowserModel.getCurUrl())
                false
            } else {
                mUrlTextEditor.setText(mBrowserModel.getCurTitle())
                true
            }
        }
    }

    private fun loadDefUrl() {
        mBrowserModel.loadUrl(mClient.mDefUrl)
        mBrowserModel.curWebPageInfo.observe(this) {
            mUrlTextEditor.setText(it.title)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(mBinding.root)
        setContentView(R.layout.main_page)
        initBrowser()
        loadDefUrl()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (mShouldGoBack) {
            if (!mBrowserModel.goBack()) finish()
        } else {
            mUrlTextEditor.clearFocus()
        }
    }
}
