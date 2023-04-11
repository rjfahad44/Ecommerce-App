package com.ft.ltd.ecommerceapp_mytask.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ft.ltd.ecommerceapp_mytask.R
import com.ft.ltd.ecommerceapp_mytask.data.listeners.CartOnClickListeners
import com.ft.ltd.ecommerceapp_mytask.data.listeners.ProductOnClickListeners
import com.ft.ltd.ecommerceapp_mytask.data.model.Categories
import com.ft.ltd.ecommerceapp_mytask.data.model.Products
import com.ft.ltd.ecommerceapp_mytask.data.model.ProductsItem
import com.ft.ltd.ecommerceapp_mytask.databinding.RvItemCartBinding
import com.ft.ltd.ecommerceapp_mytask.databinding.RvItemCategoryBinding
import com.ft.ltd.ecommerceapp_mytask.databinding.RvItemProductBinding

class ProductCartAdapter(private val cartOnClickListeners: CartOnClickListeners) : RecyclerView.Adapter<ProductCartAdapter.ProductCartViewHolder>() {

    private var productsList = emptyList<ProductsItem>()
    private var quantity: Int = 1
    @SuppressLint("NotifyDataSetChanged")
    fun submitData(items: ArrayList<ProductsItem>){
        productsList = items
        notifyDataSetChanged()
    }

    inner class ProductCartViewHolder(val binding: RvItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductsItem) {
            val ctx = binding.root.context
            Glide.with(ctx).load(item.image).placeholder(R.drawable.product_placeholder_img).centerCrop().into(binding.productImg)
            binding.productName.text = item.title

            binding.productQuantityPlus.setOnClickListener {
                val price = item.price/quantity
                quantity++
                item.price = item.price + price
                cartOnClickListeners.productQuantityPlusOnClickListener(absoluteAdapterPosition, item)
            }
            binding.productQuantityMinus.setOnClickListener {
                if (quantity>1){
                    val price = item.price/quantity
                    quantity--
                    item.price = item.price - price
                    cartOnClickListeners.productQuantityMinusOnClickListener(absoluteAdapterPosition, item)
                }
            }

            binding.pricingQuantity.text = "${quantity}"
            binding.productPrice.text = "$ ${String. format("%.1f", item.price)}"

            binding.productDelete.setOnClickListener {
                cartOnClickListeners.productDeleteOnClickListener(absoluteAdapterPosition, item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductCartViewHolder(
        RvItemCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ProductCartViewHolder, position: Int) {
        holder.bind(productsList[position])
    }

    override fun getItemCount(): Int = productsList.size
}