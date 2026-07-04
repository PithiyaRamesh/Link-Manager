package com.redvings.linkmanager.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.redvings.linkmanager.databinding.ItemCollectionVerticalBinding
import com.redvings.linkmanager.models.CollectionModel

class SelectCollectionAdapter(private val callback: CollectionCallback) :
    RecyclerView.Adapter<SelectCollectionAdapter.ViewHolder>() {
    private val mListAttached = arrayListOf<CollectionModel>()
    private var currentSelection = -1

    inner class ViewHolder(private val binding: ItemCollectionVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData() = with(binding) {
            val data = mListAttached[adapterPosition]
            tvName.text = data.name
            imageTick.isVisible = currentSelection == adapterPosition
        }


        init {
            binding.root.setOnClickListener {
                onItemSelectionChanged(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCollectionVerticalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = mListAttached.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData()
    }

    fun onItemSelectionChanged(position: Int) {
        val oldSelection = currentSelection
        currentSelection = position
        notifyItemChanged(oldSelection)
        notifyItemChanged(currentSelection)
    }


    fun addList(list: List<CollectionModel>) {
        val oldSize = itemCount
        mListAttached.addAll(list)
        notifyItemRangeInserted(oldSize, itemCount)
    }

    fun addItem(item: CollectionModel) {
        mListAttached.add(item)
        notifyItemInserted(itemCount)
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


    interface CollectionCallback {
        fun onItemSelectionChanged(item: CollectionModel)
    }
}