package com.miku.mubb.bean

import java.io.Serializable
import java.sql.Timestamp

data class WebPageInfo constructor(val title: String, val url: String, val timestamp: Timestamp? = null) : Serializable
