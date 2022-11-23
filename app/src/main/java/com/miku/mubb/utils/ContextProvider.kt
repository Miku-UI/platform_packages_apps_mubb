package com.miku.mubb.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class ContextProvider : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        fun getContext(): Context {
            return context
        }

        fun getString(resId: Int): String {
            return context.getString(resId)
        }
    }
}
