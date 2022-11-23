package com.miku.mubb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.miku.mubb.viewmodel.BrowserModel
import com.miku.mubb.webview.WebViewClientMiku

class MainActivity : ComponentActivity() {
    // private val mBinding: MainPageBinding by lazy { MainPageBinding.inflate(layoutInflater) }
    private var mAsBuiltinBrowser = false
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
        val btnBack: TextView = findViewById(R.id.iv_back)
        val btnForward: TextView = findViewById(R.id.iv_forward)
        val btnMiku: ImageView = findViewById(R.id.iv_miku)
        btnBack.setOnClickListener {
            onBackPressed()
        }
        btnForward.setOnClickListener {
            mBrowserModel.goForward()
        }
        btnMiku.setOnClickListener {
            mBrowserModel.returnHomePage()
        }
    }

    private fun loadUrl(url: String?) {
        if (url == null) {
            mAsBuiltinBrowser = false
            mBrowserModel.loadUrl(mBrowserModel.mDefUrl)
        } else {
            mAsBuiltinBrowser = true
            mBrowserModel.loadUrl(url)
        }
        mBrowserModel.curWebPageInfo.observe(this) {
            mUrlTextEditor.setText(it.title)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(mBinding.root)
        setContentView(R.layout.main_page)
        initBrowser()
        val url = intent.getStringExtra(INPUT_URL)
        loadUrl(url)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (mShouldGoBack) {
            if (!mBrowserModel.goBack()) finish()
        } else {
            mUrlTextEditor.clearFocus()
        }
    }

    companion object {
        const val TAG = "MainActivity"
        const val INPUT_URL = "inputUrl"

        fun startActivity(context: Context, url: String) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(INPUT_URL, url)
            context.startActivity(intent)
        }
    }
}
