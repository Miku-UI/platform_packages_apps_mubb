package com.miku.mubb

import android.app.Activity
import android.os.Bundle
import com.miku.mubb.utils.LogRecorder

class OpenActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        val url = intent.data.toString()
        LogRecorder.d(TAG, "Prepare to start MUUB as Built-in Browser, url is $url")
        MainActivity.startActivity(this, url)
    }

    companion object {
        const val TAG = "OpenActivity"
    }
}
