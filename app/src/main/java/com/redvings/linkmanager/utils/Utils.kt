package com.redvings.linkmanager.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.textfield.TextInputLayout
import com.redvings.linkmanager.R
import java.io.Serializable


object Utils {

    object Commons {
        const val EDIT_LINK = 0
        const val DELETE_LINK = 1
        const val CHANGE_COLLECTION = 2

        const val DATA_BUNDLE = "DATA_BUNDLE"
        const val IN_APP = "in_app"
    }

    fun FragmentActivity.screenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    inline val String.httpLink get() = if (startsWith("http")) this else "http://$this"

    inline val CharSequence?.isValidUrl
        get() = !this.isNullOrBlank() && Patterns.WEB_URL.matcher(
            this
        ).matches()

    fun Any.eLog(msg: String) {
        val tag = "LinkManager==>${this::class.java.simpleName}"
        Log.e(tag, msg)
    }

    inline fun <reified T : ViewBinding> Context.inflateBinding(): T {
        val inflateMethod = T::class.java.getMethod("inflate", LayoutInflater::class.java)
        return inflateMethod.invoke(null, LayoutInflater.from(this)) as T
    }

    fun Context.openLinkInBrowser(link: String) {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(link.httpLink)
            startActivity(i)
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.msg_unable_to_open_link), Toast.LENGTH_SHORT)
                .show()
            eLog("Unable to open browser")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun RecyclerView.Adapter<*>.notifyChangeAll() {
        this.notifyDataSetChanged()
    }

    fun TextView.setTextFormatted(@StringRes id: Int, vararg formatArgs: String) {
        text = String.format(context.getString(id), *formatArgs)
    }

    fun EditText.stringText(trim: Boolean = true) =
        if (trim) text.toString().trim() else text.toString()

    inline val EditText.isNotBlank get() = text.toString().isNotBlank()
    inline val TextInputLayout.isNotNullBlank
        get() = this.editText?.text?.toString().isNullOrBlank().not()
    inline val TextInputLayout.isNullBlank get() = this.editText?.text?.toString().isNullOrBlank()

    fun TextInputLayout.setEmptyCheck(s: String): TextInputLayout {
        editText?.doAfterTextChanged {
            if (it.isNullOrBlank()) {
                isErrorEnabled = true
                error = s
            } else {
                error = null
                isErrorEnabled = false
            }
        }
        return this
    }

    fun TextInputLayout.setEmptyCheck(@StringRes id: Int): TextInputLayout {
        return setEmptyCheck(context.getString(id))
    }

    fun TextInputLayout.checkValidUrl(s: String): TextInputLayout {
        editText?.doAfterTextChanged {
            if (it.isNullOrBlank()) {
                return@doAfterTextChanged
            }
            if (it.isValidUrl) {
                error = null
                isErrorEnabled = false
            } else {
                isErrorEnabled = true
                error = s
            }
        }
        return this
    }

    fun TextInputLayout.checkValidUrl(@StringRes id: Int): TextInputLayout {
        return checkValidUrl(context.getString(id))
    }

    fun TextInputLayout.validate() {
        editText?.text = editText?.text
        if (!error.isNullOrBlank()) {
            editText?.requestFocus()
            throw Exception(error.toString())
        }
    }


    inline fun <reified T : Serializable?> Intent.getSerializableData(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSerializableExtra(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            getSerializableExtra(key) as? T
        }
    }

    inline fun safely(action: () -> Unit) {
        try {
            action()
        } catch (_: Exception) {
        }
    }

    fun checkEmpty(vararg textInputLayouts: TextInputLayout, action: (valid: Boolean) -> Unit) {
        val list: List<TextInputLayout> = textInputLayouts.toList()
        list.forEach { layout ->
            layout.editText?.doAfterTextChanged {
                action(list.all { it.isNotNullBlank })
            }
        }
    }

    fun checkAllEmptyOrAllFilled(
        vararg textInputLayouts: TextInputLayout,
        action: (valid: Boolean) -> Unit
    ) {
        val list: List<TextInputLayout> = textInputLayouts.toList()
        list.forEach {
            it.editText?.doAfterTextChanged {
                val valid = list.all { it.isNullBlank } || list.all { it.isNotNullBlank }
                if (valid) {
                    list.forEach { edt -> edt.error = null }
                }
                action(valid)
            }
        }
    }
}