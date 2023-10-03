package com.redvings.linkmanager.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.redvings.linkmanager.R
import com.redvings.linkmanager.utils.AppPreferences

open class BaseActivity : AppCompatActivity(){
    val preferences by lazy { AppPreferences.getInstance(applicationContext) }

    fun <V : ViewBinding> showFragmentDialog(
        dBinding: V,
        callBack: (alertBinding: V, alert: AlertDialog) -> Unit
    ) {
        val builder = AlertDialog.Builder(this, R.style.dialogTheme)
        builder.setView(dBinding.root)
        val alert = builder.create()
        alert.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        alert.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        callBack(dBinding, alert)
    }
}