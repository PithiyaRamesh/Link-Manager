package com.redvings.linkmanager.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.redvings.linkmanager.databinding.ItemLinkCollectionBinding
import com.redvings.linkmanager.models.CollectionModel

class TabsRecyclerAdapter(val callback: CollectionCallback) :
    RecyclerView.Adapter<TabsRecyclerAdapter.ViewHolder>() {
    private val mListAttached = arrayListOf<CollectionModel>()
    private inline val size get() = mListAttached.size
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
        return ViewHolder(
            ItemLinkCollectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
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
        callback.onItemSelectionChanged(mListAttached[position])
    }


    fun addList(list: List<CollectionModel>) {
        val oldSize = size
        mListAttached.addAll(list)
        notifyItemRangeInserted(oldSize, size)
        if (oldSize <= 0 && size > 0) { // Notify for first item entry
            callback.onItemSelectionChanged(list[0])
        }
    }

    fun addItem(item: CollectionModel) {
        val oldSize = size
        mListAttached.add(size, item)
        if (oldSize <= 0 && size > 0) { // Notify for first item entry
            callback.onItemSelectionChanged(mListAttached[0])
        }
        notifyItemInserted(size - 1)
        notifyItemChanged(itemCount - 2)
    }

    fun getList(): ArrayList<CollectionModel> {
        return mListAttached
    }

    fun removeItem(id: String) {
        val index = mListAttached.indexOfFirst { id == it.id }
        if (index != -1) {
            mListAttached.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun getSelectedCollection(): CollectionModel? {
        return if (size <= 0) null else mListAttached[currentSelection]
    }

    interface CollectionCallback {
        fun onItemSelectionChanged(item: CollectionModel)
        fun onAddItemClicked()
    }
}