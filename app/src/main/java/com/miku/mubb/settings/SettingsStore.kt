package com.miku.mubb.settings

import androidx.preference.PreferenceManager
import com.miku.mubb.utils.ContextProvider

// Const
const val ENABLE_JAVASCRIPT = "enable_javascript"
const val ENABLE_ZOOM = "enable_zoom"
const val ENABLE_LOG = "enable_log"

// Global Config
val shouldLog: Boolean by lazy {
    PreferenceManager.getDefaultSharedPreferences(ContextProvider.getContext()).getBoolean(
        ENABLE_LOG, false
    )
}
