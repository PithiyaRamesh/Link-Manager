package com.redvings.linkmanager.utils

import android.util.Log

object Utils {
    fun Any.eLog(msg: String) {
        val tag = "LinkManager==>${this::class.java.simpleName}"
        Log.e(tag, msg)
    }
}