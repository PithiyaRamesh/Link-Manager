package com.redvings.linkmanager.ui.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatDelegate
import com.redvings.linkmanager.databinding.AppearancePopupBinding

class AppearancePopup {
    var popup: PopupWindow? = null

    fun showPopupWindow(view: View, callback: (Int) -> Unit) {
        popup = PopupWindow(
            view,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            false
        )

        val binding = AppearancePopupBinding.inflate(LayoutInflater.from(view.context), null, false)
        popup?.contentView = binding.root
        popup?.isOutsideTouchable = true
        popup?.showAsDropDown(view, -5, -5)

        //Handler for clicking on the inactive zone of the window
//        binding.root.setOnTouchListener { v, event -> //Close the window when clicked
//            popup?.dismiss()
//            true
//        }

        binding.tvDark.setOnClickListener {
            callback(AppCompatDelegate.MODE_NIGHT_YES)
            popup?.dismiss()
        }
        binding.tvLight.setOnClickListener {
            callback(AppCompatDelegate.MODE_NIGHT_NO)
            popup?.dismiss()
        }
        binding.tvSystem.setOnClickListener {
            callback(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            popup?.dismiss()
        }
    }


    fun close() {
        popup?.dismiss()
    }
}