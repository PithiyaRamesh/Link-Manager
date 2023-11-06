package com.redvings.linkmanager.base

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.redvings.linkmanager.R
import com.redvings.linkmanager.ui.dialogs.ProgressDialog
import com.redvings.linkmanager.utils.AppPreferences

open class BaseActivity : AppCompatActivity() {
    val preferences by lazy { AppPreferences.getInstance(applicationContext) }

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(this)
    }

    /*    fun <V : ViewBinding> showFragmentDialog(
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
        }*/

    fun getTextForAppearance(appearance: Int): String {
        return when (appearance) {
            AppCompatDelegate.MODE_NIGHT_NO -> getString(R.string.text_light)
            AppCompatDelegate.MODE_NIGHT_YES -> getString(R.string.text_dark)
            else -> getString(R.string.text_system)
        }
    }

    fun showTwoActionDialog(
        title: String?,
        description: String,
        posText: String,
        negText: String,
        positiveClick: () -> Unit
    ) {
        MaterialAlertDialogBuilder(this).apply {
            setPositiveButton(
                posText
            ) { dialog, which ->
                positiveClick()
                dialog.dismiss()
            }
            setNegativeButton(negText) { dialog, which ->
                dialog.dismiss()
            }
        }.create().apply {
            setTitle(title)
            setMessage(description)
        }.show()
    }

    fun showLoader(show: Boolean) {
        if (show) {
            if (!progressDialog.isShowing)
                progressDialog.show()
        } else {
            if (progressDialog.isShowing)
                progressDialog.dismiss()
        }
    }
}