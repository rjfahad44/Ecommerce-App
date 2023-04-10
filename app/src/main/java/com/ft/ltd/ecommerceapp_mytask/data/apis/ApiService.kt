package com.ft.ltd.ecommerceapp_mytask.data.apis


import com.haroldadmin.cnradapter.NetworkResponse
import com.ft.ltd.ecommerceapp_mytask.data.model.Categories
import com.ft.ltd.ecommerceapp_mytask.data.model.ErrorResponse
import com.ft.ltd.ecommerceapp_mytask.data.model.Products
import retrofit2.http.GET

interface ApiService {
    @GET("products/categories")
    suspend fun getProductCategories(): NetworkResponse<Categories, ErrorResponse>

    @GET("products")
    suspend fun getProducts(): NetworkResponse<Products, ErrorResponse>
}