package com.redvings.linkmanager.utils

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

object Utils {
    fun Any.eLog(msg: String) {
        val tag = "LinkManager==>${this::class.java.simpleName}"
        Log.e(tag, msg)
    }
    inline fun <reified T : ViewBinding> Context.inflateBinding(): T {
        val inflateMethod = T::class.java.getMethod("inflate", LayoutInflater::class.java)
        return inflateMethod.invoke(null, LayoutInflater.from(this)) as T
    }
}