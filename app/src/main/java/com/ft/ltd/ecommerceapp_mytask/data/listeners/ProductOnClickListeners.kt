package com.ft.ltd.ecommerceapp_mytask.data.listeners

import com.ft.ltd.ecommerceapp_mytask.data.model.ProductsItem

interface ProductOnClickListeners {
    fun productDetailsOnClickListener(productsItem: ProductsItem)
    fun addToCartOnClickListener(productsItem: ProductsItem)
}