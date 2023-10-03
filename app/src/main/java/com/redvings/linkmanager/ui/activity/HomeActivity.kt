package com.redvings.linkmanager.ui.activity

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.redvings.linkmanager.base.BaseActivity
import com.redvings.linkmanager.databinding.ActivityMainBinding
import com.redvings.linkmanager.models.TabsModel
import com.redvings.linkmanager.ui.adapters.TabsRecyclerAdapter
import com.redvings.linkmanager.utils.Utils.eLog

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val tabsRecyclerCallback: TabsRecyclerAdapter.CollectionCallback by lazy {
        object : TabsRecyclerAdapter.CollectionCallback {
            override fun onItemSelectionChanged(item: TabsModel) {
                eLog("itemSelected: $item")
            }

            override fun onAddItemClicked() {
            }
        }
    }
    private val tabsRecyclerAdapter by lazy {
        TabsRecyclerAdapter(tabsRecyclerCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        installSplashScreen()
        setContentView(binding.root)
        setTabsRecyclerView()
    }

    private fun setTabsRecyclerView() {
        tabsRecyclerAdapter.addList(preferences.tabs)
        binding.recyclerTabs.adapter = tabsRecyclerAdapter
    }
}