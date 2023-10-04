package com.redvings.linkmanager.ui.activity

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.redvings.linkmanager.base.BaseActivity
import com.redvings.linkmanager.databinding.ActivityMainBinding
import com.redvings.linkmanager.models.LinkModel
import com.redvings.linkmanager.models.TabsModel
import com.redvings.linkmanager.ui.adapters.LinksRecyclerAdapter
import com.redvings.linkmanager.ui.adapters.TabsRecyclerAdapter
import com.redvings.linkmanager.utils.Utils.eLog
import java.util.ArrayList

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    private val linksRecyclerCallback by lazy {
        object : LinksRecyclerAdapter.CollectionCallback{
            override fun onItemClicked(item: LinkModel) {
                eLog("onItemClicked: $item")
            }
        }
    }

    private val tabsRecyclerCallback: TabsRecyclerAdapter.CollectionCallback by lazy {
        object : TabsRecyclerAdapter.CollectionCallback {
            override fun onItemSelectionChanged(item: TabsModel) {
                eLog("List: $item")
                setLinksList(item.links)
            }

            override fun onAddItemClicked() {
                tabsRecyclerAdapter.addItem(
                    TabsModel(
                        tabsRecyclerAdapter.itemCount.toString(),
                        "New list",
                        arrayListOf()
                    )
                )

                preferences.tabs = tabsRecyclerAdapter.getList()
            }
        }
    }

    private val tabsRecyclerAdapter by lazy {
        TabsRecyclerAdapter(tabsRecyclerCallback)
    }

    private val linksRecyclerAdapter: LinksRecyclerAdapter by lazy {
        LinksRecyclerAdapter(linksRecyclerCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        installSplashScreen()
        setContentView(binding.root)
        setTabsRecyclerView()
        setLinksRecyclerView()
    }

    private fun setTabsRecyclerView() {
        binding.recyclerTabs.adapter = tabsRecyclerAdapter
        preferences.tabs?.let { tabsRecyclerAdapter.addList(it) }
    }

    private fun setLinksRecyclerView() {
        binding.recyclerLinks.adapter = linksRecyclerAdapter
    }

    private fun setLinksList(links: ArrayList<LinkModel>?) {
        linksRecyclerAdapter.replaceList(links)
    }
}