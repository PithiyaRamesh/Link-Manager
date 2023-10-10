package com.redvings.linkmanager.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import com.airbnb.lottie.LottieAnimationView
import com.redvings.linkmanager.R

class ProgressDialog(context: Context) : Dialog(context) {

    init {
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null, false)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(false)
        setContentView(view)
        view.findViewById<LottieAnimationView>(R.id.animation_view).playAnimation()
    }
}