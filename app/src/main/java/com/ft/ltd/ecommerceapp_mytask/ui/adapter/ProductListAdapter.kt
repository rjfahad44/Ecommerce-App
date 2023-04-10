package com.ft.ltd.ecommerceapp_mytask.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ft.ltd.ecommerceapp_mytask.R
import com.ft.ltd.ecommerceapp_mytask.data.model.Categories
import com.ft.ltd.ecommerceapp_mytask.data.model.Products
import com.ft.ltd.ecommerceapp_mytask.data.model.ProductsItem
import com.ft.ltd.ecommerceapp_mytask.databinding.RvItemCategoryBinding
import com.ft.ltd.ecommerceapp_mytask.databinding.RvItemProductBinding

class ProductListAdapter(private val itemClickListener: (ProductsItem) -> Unit) : RecyclerView.Adapter<ProductListAdapter.ProductCategoriesViewHolder>() {

    private var productsList = emptyList<ProductsItem>()
    @SuppressLint("NotifyDataSetChanged")
    fun submitData(items: ArrayList<ProductsItem>){
        productsList = items
        notifyDataSetChanged()
    }

    inner class ProductCategoriesViewHolder(val binding: RvItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductsItem) {
            val ctx = binding.root.context
            Glide.with(ctx).load(item.image).placeholder(R.drawable.product_placeholder_img).centerCrop().into(binding.productImg)
            binding.productPriceTv.text = "$ ${item.price}"
            binding.productRatingBar.rating = item.rating.rate.toFloat()
            binding.root.setOnClickListener { itemClickListener.invoke(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductCategoriesViewHolder(
        RvItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ProductCategoriesViewHolder, position: Int) {
        holder.bind(productsList[position])
    }

    override fun getItemCount(): Int = productsList.size
}