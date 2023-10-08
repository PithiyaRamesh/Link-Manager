package com.redvings.linkmanager.ui.activity

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.redvings.linkmanager.R
import com.redvings.linkmanager.base.BaseActivity
import com.redvings.linkmanager.databinding.ActivitySettingsBinding

class SettingsActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.tvTitle.setText(R.string.text_settings)
        setClicks()
    }

    private fun setClicks() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }
}