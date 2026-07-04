package com.redvings.linkmanager.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.redvings.linkmanager.R
import com.redvings.linkmanager.base.BaseFragment
import com.redvings.linkmanager.databinding.FragmentCollectionListBinding
import com.redvings.linkmanager.models.CollectionModel
import com.redvings.linkmanager.ui.adapters.SelectCollectionAdapter

class CollectionListFragment : BaseFragment<FragmentCollectionListBinding>() {
    val collectionAdapter by lazy {
        SelectCollectionAdapter(object : SelectCollectionAdapter.CollectionCallback {
            override fun onItemSelectionChanged(item: CollectionModel) {

            }
        })
    }

    companion object {
        fun newInstance() = CollectionListFragment()
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentCollectionListBinding {
        return FragmentCollectionListBinding.inflate(inflater)
    }

    override fun bindData() {
        binding.toolbar.ivBack.setOnClickListener { childFragmentManager.popBackStackImmediate(this::class.java.simpleName, 0) }
        binding.toolbar.tvTitle.setText(R.string.text_move_to_another_tab)
        preferences.tabs?.let { collectionAdapter.addList(it) }
        binding.recyclerCollections.adapter = collectionAdapter
    }
}