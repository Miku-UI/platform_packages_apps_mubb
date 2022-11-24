package com.miku.mubb.utils

import android.util.Log
import com.miku.mubb.settings.shouldLog

object LogRecorder {
    fun d(tag: String, content: String) {
        if (shouldLog) Log.d(tag, content)
    }
}
