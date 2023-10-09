package com.redvings.linkmanager.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import com.redvings.linkmanager.R
import com.redvings.linkmanager.base.BaseActivity
import com.redvings.linkmanager.databinding.ActivitySettingsBinding
import com.redvings.linkmanager.ui.dialogs.AppearancePopup
import com.redvings.linkmanager.utils.AppPreferences.Keys

class SettingsActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.tvTitle.setText(R.string.text_settings)
        setClicks()
    }

    override fun onStart() {
        super.onStart()
        binding.labelSelectedAppearance.text = getTextForAppearance(
            preferences.getInt(Keys.SELECTED_APPEARANCE, MODE_NIGHT_FOLLOW_SYSTEM)
        )
    }

    private fun setClicks() {
        binding.toolbar.ivBack.setOnClickListener(this)
        binding.labelAppearance.setOnClickListener(this)
        binding.labelSelectedAppearance.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when (p0) {
            binding.toolbar.ivBack -> {
                finish()
            }

            binding.labelAppearance, binding.labelSelectedAppearance -> {
                AppearancePopup().showPopupWindow(binding.labelSelectedAppearance) {
                    preferences.putInt(Keys.SELECTED_APPEARANCE, it)
                    AppCompatDelegate.setDefaultNightMode(it)
                    binding.labelSelectedAppearance.text = getTextForAppearance(it)
                }
            }
        }
    }
}