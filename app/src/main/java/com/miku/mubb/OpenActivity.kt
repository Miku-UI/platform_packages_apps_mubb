package com.miku.mubb

import android.app.Activity
import android.os.Bundle

class OpenActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        val url = intent.data.toString()
        MainActivity.startActivity(this, url)
    }
}
