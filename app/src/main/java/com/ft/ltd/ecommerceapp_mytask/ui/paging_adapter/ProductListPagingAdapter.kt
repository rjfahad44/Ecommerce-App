package com.ft.ltd.ecommerceapp_mytask.ui.paging_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ft.ltd.ecommerceapp_mytask.databinding.RvItemProductBinding


private val Comparator = object : DiffUtil.ItemCallback<DummyPagerModel>() {
    override fun areItemsTheSame(oldItem: DummyPagerModel, newItem: DummyPagerModel) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: DummyPagerModel, newItem: DummyPagerModel) = oldItem == newItem
}

class ProductListPagingAdapter(val itemClickListener: (DummyPagerModel) -> Unit) :
    PagingDataAdapter<DummyPagerModel, ProductListPagingAdapter.ProductListViewHolder>(Comparator) {

    inner class ProductListViewHolder(val binding: RvItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DummyPagerModel) {
            val ctx = binding.root.context

            binding.root.setOnClickListener { itemClickListener.invoke(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductListViewHolder(RvItemProductBinding.inflate(
        LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}