package com.redvings.linkmanager.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.redvings.linkmanager.R
import com.redvings.linkmanager.base.BaseActivity
import com.redvings.linkmanager.databinding.ActivityMainBinding
import com.redvings.linkmanager.models.CollectionModel
import com.redvings.linkmanager.models.LinkModel
import com.redvings.linkmanager.ui.adapters.LinksRecyclerAdapter
import com.redvings.linkmanager.ui.adapters.TabsRecyclerAdapter
import com.redvings.linkmanager.ui.dialogs.AddCollectionDialog
import com.redvings.linkmanager.ui.dialogs.AddLinkDialog
import com.redvings.linkmanager.ui.dialogs.LinkOptionsPopup
import com.redvings.linkmanager.utils.AppPreferences.Keys
import com.redvings.linkmanager.utils.Utils.Commons
import com.redvings.linkmanager.utils.Utils.eLog
import com.redvings.linkmanager.utils.Utils.openLinkInBrowser
import com.redvings.linkmanager.utils.Utils.screenWidth

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isInitialLayout = true
    private val adViewTop by lazy {
        AdView(this)
    }

    private val adViewCallbacks by lazy {
        object : AdListener() {
            override fun onAdClicked() {
                eLog("=====onAdClicked()=====")
            }

            override fun onAdClosed() {
                eLog("=====onAdClosed()=====")
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                eLog("=====onAdFailedToLoad(): ${p0.message}=====")
            }

            override fun onAdImpression() {
                eLog("=====onAdImpression()=====")
            }

            override fun onAdLoaded() {
                eLog("=====onAdLoaded()=====")
            }

            override fun onAdOpened() {
                eLog("=====onAdOpened()=====")
            }

            override fun onAdSwipeGestureClicked() {
                eLog("=====onAdSwipeGestureClicked()=====")
            }
        }
    }

    private val adSize: AdSize
        get() {
            var adWidthPixels = binding.bannerAdTopContainer.width.toFloat()

            // If the ad hasn't been laid out, default to the full screen width.
            if (adWidthPixels == 0f) {
                adWidthPixels = screenWidth().toFloat()
            }

            val density = resources.displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()

            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }

    private val linksRecyclerCallback by lazy {
        object : LinksRecyclerAdapter.CollectionCallback {
            override fun onOptionsClicked(item: LinkModel, view: View) {
                LinkOptionsPopup().showPopupWindow(view) {
                    when (it) {
                        Commons.EDIT_LINK -> {
                            showEditLinkDialog(item)
                        }

                        Commons.DELETE_LINK -> {
                            showTwoActionDialog(
                                title = null,
                                "Delete ${item.name}?",
                                "Delete",
                                "Cancel"
                            ) {
                                linksRecyclerAdapter.removeItem(item.id)
                                preferences.tabs = tabsRecyclerAdapter.getList()
                            }
                        }

                        Commons.CHANGE_COLLECTION -> {

                        }
                    }
                }
            }

            override fun onItemClicked(item: LinkModel) {
                if (preferences.openWith == Commons.IN_APP) {
                    startActivity(
                        Intent(
                            this@HomeActivity,
                            WebViewActivity::class.java
                        ).also { intent: Intent -> intent.putExtra(Commons.DATA_BUNDLE, item) }
                    )
                } else {
                    item.link?.let { openLinkInBrowser(it) }
                }
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
        AppCompatDelegate.setDefaultNightMode(
            preferences.getInt(
                Keys.SELECTED_APPEARANCE,
                MODE_NIGHT_FOLLOW_SYSTEM
            )
        )
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdMob()
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

        binding.llSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun showAddCollectionDialog() {
        AddCollectionDialog {
            tabsRecyclerAdapter.addItem(it)
            preferences.tabs = tabsRecyclerAdapter.getList() //Update session list
        }.show(supportFragmentManager, "AddCollectionDialog")
    }

    private fun showAddLinkDialog(collection: CollectionModel) {
        AddLinkDialog(getString(R.string.text_add_to_holder, collection.name)) {
            linksRecyclerAdapter.addItem(collection, it)
            preferences.tabs = tabsRecyclerAdapter.getList() //Update session list
        }.show(supportFragmentManager, "AddLinkDialog")
    }

    private fun showEditLinkDialog(linkItem: LinkModel) {
        AddLinkDialog(getString(R.string.edit_s, linkItem.name), linkItem) {
            linksRecyclerAdapter.updateItem(it)
            preferences.tabs = tabsRecyclerAdapter.getList() //Update session list
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

    private fun initAdMob() {
        MobileAds.initialize(this) {
            eLog("======MobileAds initialization complete======")
        }

        adViewTop.adListener = adViewCallbacks
        adViewTop.adUnitId = getString(R.string.test_ad_unit_id)
        binding.bannerAdTopContainer.addView(adViewTop)

        binding.bannerAdTopContainer.viewTreeObserver.addOnGlobalLayoutListener {
            if (isInitialLayout) {
                isInitialLayout = false
                loadTopBannerAd()
            }
        }
    }

    private fun loadTopBannerAd() {
        adViewTop.setAdSize(adSize)

        val adRequest = AdRequest
            .Builder()
            .build()

        adViewTop.loadAd(adRequest)
    }
}