package com.redvings.linkmanager.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.redvings.linkmanager.R
import com.redvings.linkmanager.base.BaseFragment
import com.redvings.linkmanager.databinding.FragmentHomeBinding
import com.redvings.linkmanager.models.CollectionModel
import com.redvings.linkmanager.models.LinkModel
import com.redvings.linkmanager.ui.activity.WebViewActivity
import com.redvings.linkmanager.ui.adapters.LinksRecyclerAdapter
import com.redvings.linkmanager.ui.adapters.TabsRecyclerAdapter
import com.redvings.linkmanager.ui.dialogs.AddCollectionDialog
import com.redvings.linkmanager.ui.dialogs.AddLinkDialog
import com.redvings.linkmanager.ui.dialogs.LinkOptionsPopup
import com.redvings.linkmanager.utils.Utils
import com.redvings.linkmanager.utils.Utils.copyToClipboard
import com.redvings.linkmanager.utils.Utils.eLog
import com.redvings.linkmanager.utils.Utils.openLinkInBrowser
import com.redvings.linkmanager.utils.Utils.showToast

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private var param1: String? = null
    private var param2: String? = null


    private val linksRecyclerCallback by lazy {
        object : LinksRecyclerAdapter.CollectionCallback {
            override fun onOptionsClicked(item: LinkModel, view: View) {
                LinkOptionsPopup().showPopupWindow(view) {
                    when (it) {
                        Utils.Commons.EDIT_LINK -> {
                            showEditLinkDialog(item)
                        }

                        Utils.Commons.DELETE_LINK -> {
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

                        Utils.Commons.CHANGE_COLLECTION -> {

                        }
                    }
                }
            }

            override fun onItemClicked(item: LinkModel) {
                if (preferences.openWith == Utils.Commons.IN_APP) {
                    startActivity(
                        Intent(
                            requireContext(),
                            WebViewActivity::class.java
                        ).also { intent: Intent -> intent.putExtra(Utils.Commons.DATA_BUNDLE, item) }
                    )
                } else {
                    item.link?.let { context?.openLinkInBrowser(it) }
                }
            }

            override fun onCopyLinkClicked(item: LinkModel) {
                item.link?.let {
                    context?.copyToClipboard(it)
                    showToast(R.string.msg_link_copied_to_clipboard)
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

    companion object {
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

    override fun bindData() {
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

    private fun showEditLinkDialog(linkItem: LinkModel) {
        AddLinkDialog(getString(R.string.edit_s, linkItem.name), linkItem) {
            linksRecyclerAdapter.updateItem(it)
            preferences.tabs = tabsRecyclerAdapter.getList() //Update session list
        }.show(childFragmentManager, "AddLinkDialog")
    }

    private fun showAddCollectionDialog() {
        AddCollectionDialog {
            tabsRecyclerAdapter.addItem(it)
            preferences.tabs = tabsRecyclerAdapter.getList() //Update session list
        }.show(childFragmentManager, "AddCollectionDialog")
    }

    private fun setLinksList(links: ArrayList<LinkModel>?) {
        if (links != null) {
            linksRecyclerAdapter.replaceList(links)
        }
    }
}