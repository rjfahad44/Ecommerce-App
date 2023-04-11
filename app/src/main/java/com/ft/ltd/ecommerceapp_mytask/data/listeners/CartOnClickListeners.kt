package com.ft.ltd.ecommerceapp_mytask.data.listeners

import com.ft.ltd.ecommerceapp_mytask.data.model.ProductsItem

interface CartOnClickListeners {
    fun productQuantityMinusOnClickListener(position: Int,productsItem: ProductsItem)
    fun productQuantityPlusOnClickListener(position: Int, productsItem: ProductsItem)
    fun productDeleteOnClickListener(position: Int, productsItem: ProductsItem)
}