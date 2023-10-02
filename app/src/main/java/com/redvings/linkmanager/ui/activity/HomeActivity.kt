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
        object : TabsRecyclerAdapter.CollectionCallback{
            override fun onItemSelectionChanged(item: TabsModel) {
                eLog("itemSelected: $item")
            }

            override fun onAddItemClicked() {
                tabsRecyclerAdapter.addItem(TabsModel(tabsRecyclerAdapter.itemCount.toString(), "\uD83D\uDCD8 Work${tabsRecyclerAdapter.itemCount}"))
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
        tabsRecyclerAdapter.addItem(TabsModel("1", "\uD83D\uDCD8 Work"))
        tabsRecyclerAdapter.addItem(TabsModel("2", "\uD83D\uDCD8 Work2"))
        tabsRecyclerAdapter.addItem(TabsModel("3", "\uD83D\uDCD8 Work3"))
        tabsRecyclerAdapter.addItem(TabsModel("4", "\uD83D\uDCD8 Work4"))
        tabsRecyclerAdapter.addItem(TabsModel("5", "\uD83D\uDCD8 Work5"))
        tabsRecyclerAdapter.addItem(TabsModel("6", "\uD83D\uDCD8 Work6"))
        binding.recyclerTabs.adapter = tabsRecyclerAdapter
    }
}