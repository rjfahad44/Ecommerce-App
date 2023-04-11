package com.ft.ltd.ecommerceapp_mytask.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductsItem(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    var price: Double,
    val rating: Rating,
    val title: String
) : Parcelable