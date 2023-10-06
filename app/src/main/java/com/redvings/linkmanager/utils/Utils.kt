package com.redvings.linkmanager.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.textfield.TextInputLayout

object Utils {
    fun Any.eLog(msg: String) {
        val tag = "LinkManager==>${this::class.java.simpleName}"
        Log.e(tag, msg)
    }
    inline fun <reified T : ViewBinding> Context.inflateBinding(): T {
        val inflateMethod = T::class.java.getMethod("inflate", LayoutInflater::class.java)
        return inflateMethod.invoke(null, LayoutInflater.from(this)) as T
    }

    @SuppressLint("NotifyDataSetChanged")
    fun RecyclerView.Adapter<*>.notifyChangeAll(){
        this.notifyDataSetChanged()
    }

    fun TextView.setTextFormatted(@StringRes id: Int, vararg formatArgs: String) {
        text = String.format(context.getString(id), *formatArgs)
    }

    fun EditText.stringText(trim: Boolean = true) =
        if (trim) text.toString().trim() else text.toString()

   inline val EditText.isBlank get() = text.toString().isBlank()
   inline val EditText.isNotBlank get() = text.toString().isNotBlank()

    fun TextInputLayout.setEmptyCheck(s: String): TextInputLayout {
        editText?.doAfterTextChanged {
            error = if (it.isNullOrBlank()) s else null
        }
        return this
    }

    fun TextInputLayout.setEmptyCheck(@StringRes id: Int): TextInputLayout {
        return setEmptyCheck(context.getString(id))
    }

    fun TextInputLayout.validate() {
        editText?.text = editText?.text
        if (!error.isNullOrBlank()) {
            editText?.requestFocus()
            throw Exception(error.toString())
        }
    }

    inline fun safely(action: () -> Unit) {
        try {
            action()
        } catch (_: Exception) { }
    }
}