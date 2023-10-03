package com.redvings.linkmanager.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.redvings.linkmanager.databinding.ItemLinkCollectionBinding
import com.redvings.linkmanager.models.TabsModel

class TabsRecyclerAdapter(val callback: CollectionCallback) :
    RecyclerView.Adapter<TabsRecyclerAdapter.ViewHolder>() {
    private val mListAttached = arrayListOf<TabsModel>()
    private var currentSelection = 0

    inner class ViewHolder(private val binding: ItemLinkCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData() {
            with(binding) {
                val isAddView = adapterPosition == itemCount - 1
                imageAddItem.isVisible = isAddView
                tvCollectionName.isGone = isAddView

                if (!isAddView) {
                    val data = mListAttached[adapterPosition]
                    tvCollectionName.text = data.name
                    root.isSelected = adapterPosition == currentSelection
                }
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemSelectionChanged(adapterPosition)
            }
            binding.imageAddItem.setOnClickListener {
                callback.onAddItemClicked()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLinkCollectionBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount() = mListAttached.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData()
    }

    fun onItemSelectionChanged(position: Int) {
        val oldSelection = currentSelection
        currentSelection = position
        notifyItemChanged(oldSelection)
        notifyItemChanged(currentSelection)
        callback.onItemSelectionChanged(mListAttached[position-1])
    }


    fun addList(list: List<TabsModel>) {
        val oldSize = itemCount
        mListAttached.addAll(list)
        notifyItemRangeInserted(oldSize, itemCount)
    }

    fun addItem(item: TabsModel) {
        mListAttached.add(if (itemCount > 1) itemCount - 1 else 0, item)
        notifyItemInserted(itemCount - 2)
        notifyItemChanged(itemCount - 1)
    }

    fun getList(): ArrayList<TabsModel> {
        return mListAttached
    }

    fun removeItem(id: String) {
        val index = mListAttached.indexOfFirst { id == it.id }
        if (index != -1) {
            mListAttached.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    interface CollectionCallback {
        fun onItemSelectionChanged(item: TabsModel)
        fun onAddItemClicked()
    }
}