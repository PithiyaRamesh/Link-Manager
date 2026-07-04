package com.redvings.linkmanager.ui.activity

import android.os.Bundle
import com.redvings.linkmanager.R
import com.redvings.linkmanager.base.BaseActivity
import com.redvings.linkmanager.databinding.ActivityIsolatedBinding
import com.redvings.linkmanager.ui.fragments.CollectionListFragment

class IsolatedActivity : BaseActivity() {
    private lateinit var binding: ActivityIsolatedBinding
    override fun placeHolder() = R.id.placeHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIsolatedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(CollectionListFragment.newInstance())
    }
}