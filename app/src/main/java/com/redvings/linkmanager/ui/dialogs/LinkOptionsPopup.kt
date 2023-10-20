package com.redvings.linkmanager.ui.dialogs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatDelegate
import com.redvings.linkmanager.databinding.PopupLinkEditOptionsBinding
import com.redvings.linkmanager.utils.Utils
import com.redvings.linkmanager.utils.Utils.Commons

class LinkOptionsPopup {
    var popup: PopupWindow? = null

    @SuppressLint("ClickableViewAccessibility")
    fun showPopupWindow(view: View, callback: (Int) -> Unit) {
        popup = PopupWindow(
            view,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            false
        )

        val binding =
            PopupLinkEditOptionsBinding.inflate(LayoutInflater.from(view.context), null, false)
        popup?.contentView = binding.root
        popup?.isOutsideTouchable = true
        popup?.showAsDropDown(view, -5, -5)

        //Handler for clicking on the inactive zone of the window
        binding.root.setOnTouchListener { v, event -> //Close the window when clicked
            popup?.dismiss()
            true
        }

        binding.tvEdit.setOnClickListener {
            callback(Commons.EDIT_LINK)
        }
        binding.tvDelete.setOnClickListener {
            callback(Commons.DELETE_LINK)
        }
        binding.tvChangeCollection.setOnClickListener {
            callback(Commons.CHANGE_COLLECTION)
        }
    }


    fun close() {
        popup?.dismiss()
    }
}