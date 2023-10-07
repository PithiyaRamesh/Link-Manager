package com.redvings.linkmanager.ui.activity

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.redvings.linkmanager.base.BaseActivity
import com.redvings.linkmanager.databinding.ActivityMainBinding
import com.redvings.linkmanager.models.LinkModel
import com.redvings.linkmanager.models.CollectionModel
import com.redvings.linkmanager.ui.adapters.LinksRecyclerAdapter
import com.redvings.linkmanager.ui.adapters.TabsRecyclerAdapter
import com.redvings.linkmanager.ui.dialogs.AddCollectionDialog
import com.redvings.linkmanager.ui.dialogs.AddLinkDialog
import com.redvings.linkmanager.utils.Utils.eLog

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    private val linksRecyclerCallback by lazy {
        object : LinksRecyclerAdapter.CollectionCallback {
            override fun onItemClicked(item: LinkModel) {
                eLog("onItemClicked: $item")
            }
        }
    }

    private val tabsRecyclerCallback: TabsRecyclerAdapter.CollectionCallback by lazy {
        object : TabsRecyclerAdapter.CollectionCallback {
            override fun onItemSelectionChanged(item: CollectionModel) {
                eLog("List: $item")
                setLinksList(item.links)
            }

            override fun onAddItemClicked() {
                showAddCollectionDialog()
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
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTabsRecyclerView()
        setLinksRecyclerView()
        setClicks()
    }

    private fun setClicks() {
        binding.btnAddLink.setOnClickListener {
            val collection = tabsRecyclerAdapter.getSelectedCollection()
            if (collection == null) {
                showAddCollectionDialog()
            } else {
                showAddLinkDialog(collection)
            }
        }
    }

    private fun showAddCollectionDialog() {
        AddCollectionDialog {
            tabsRecyclerAdapter.addItem(it)
            preferences.tabs = tabsRecyclerAdapter.getList() //Update session list
        }.show(supportFragmentManager, "AddCollectionDialog")
    }

    private fun showAddLinkDialog(collection: CollectionModel) {
        AddLinkDialog(collection.name ?: "") {
            collection.links?.add(it) //will reflect in session and so in current list of adapter
            linksRecyclerAdapter.notifyItemInserted(linksRecyclerAdapter.itemCount-1)
        }.show(supportFragmentManager, "AddLinkDialog")
    }

    private fun setTabsRecyclerView() {
        binding.recyclerTabs.adapter = tabsRecyclerAdapter
        preferences.tabs?.let { tabsRecyclerAdapter.addList(it) }
    }

    private fun setLinksRecyclerView() {
        binding.recyclerLinks.adapter = linksRecyclerAdapter
    }

    private fun setLinksList(links: ArrayList<LinkModel>?) {
        if (links != null) {
            linksRecyclerAdapter.replaceList(links)
        }
    }
}