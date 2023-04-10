package com.ft.ltd.ecommerceapp_mytask.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ft.ltd.ecommerceapp_mytask.R
import com.ft.ltd.ecommerceapp_mytask.data.model.Categories
import com.ft.ltd.ecommerceapp_mytask.databinding.RvItemCategoryBinding


private val Comparator = object : DiffUtil.ItemCallback<Categories>() {
    override fun areItemsTheSame(oldItem: Categories, newItem: Categories) = oldItem == newItem
    override fun areContentsTheSame(oldItem: Categories, newItem: Categories) = oldItem == newItem
}

class ProductCategoriesAdapter(private val itemClickListener: (String) -> Unit) : RecyclerView.Adapter<ProductCategoriesAdapter.ProductCategoriesViewHolder>() {

    private var categories = emptyList<String>()
    @SuppressLint("NotifyDataSetChanged")
    fun submitData(items: Categories){
        categories = items
        notifyDataSetChanged()
    }

    inner class ProductCategoriesViewHolder(val binding: RvItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            val ctx = binding.root.context
            when(item){
                "electronics" ->{
                    Glide.with(ctx).load(R.drawable.electronics_category).centerCrop().into(binding.productImg)
                }
                "jewelery" ->{
                    Glide.with(ctx).load(R.drawable.jewellery_category).centerCrop().into(binding.productImg)
                }
                "men's clothing" ->{
                    Glide.with(ctx).load(R.drawable.men_cloth_category).centerCrop().into(binding.productImg)
                }
                "women's clothing" ->{
                    Glide.with(ctx).load(R.drawable.womens_cloth_category).centerCrop().into(binding.productImg)
                }
            }
            binding.productCategoryNameTv.text = item
            binding.productStockTv.text = "5"
            binding.root.setOnClickListener { itemClickListener.invoke(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductCategoriesViewHolder(RvItemCategoryBinding.inflate(
        LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ProductCategoriesViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}