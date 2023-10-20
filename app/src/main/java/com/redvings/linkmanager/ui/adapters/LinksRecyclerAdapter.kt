package com.redvings.linkmanager.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.redvings.linkmanager.databinding.ItemLinkBinding
import com.redvings.linkmanager.models.CollectionModel
import com.redvings.linkmanager.models.LinkModel
import com.redvings.linkmanager.utils.Utils.notifyChangeAll

class LinksRecyclerAdapter(val callback: CollectionCallback) :
    RecyclerView.Adapter<LinksRecyclerAdapter.ViewHolder>() {
    private var mListAttached = arrayListOf<LinkModel>()

    inner class ViewHolder(private val binding: ItemLinkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: LinkModel) {
            with(binding) {
                tvName.text = item.name
                tvDesc.text = item.description
                tvLink.text = item.link
            }
        }

        init {
            binding.root.setOnClickListener {
                callback.onItemClicked(mListAttached[adapterPosition])
            }

            binding.ivLinkOptions.setOnClickListener {
                callback.onOptionsClicked(mListAttached[adapterPosition], binding.ivLinkOptions)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLinkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = mListAttached.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(mListAttached[position])
    }

    fun replaceList(list: ArrayList<LinkModel>) {
        mListAttached = list
        notifyChangeAll()
    }

    fun addItem(collection: CollectionModel, item: LinkModel) {
        collection.links?.add(item)
        notifyItemInserted(itemCount - 1)
    }

    fun getList(): ArrayList<LinkModel> {
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
        fun onItemClicked(item: LinkModel)

        fun onOptionsClicked(item: LinkModel, view: View)
    }
}