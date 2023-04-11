package com.ft.ltd.ecommerceapp_mytask.data.apis


import com.haroldadmin.cnradapter.NetworkResponse
import com.ft.ltd.ecommerceapp_mytask.data.model.Categories
import com.ft.ltd.ecommerceapp_mytask.data.model.ErrorResponse
import com.ft.ltd.ecommerceapp_mytask.data.model.Products
import com.ft.ltd.ecommerceapp_mytask.ui.paging_adapter.DummyPagerModel
import com.ft.ltd.ecommerceapp_mytask.ui.paging_adapter.PagedResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("products/categories")
    suspend fun getProductCategories(): NetworkResponse<Categories, ErrorResponse>

    @GET("products")
    suspend fun getProducts(): NetworkResponse<Products, ErrorResponse>

    @GET("as_per_api_path/{id}") // dummy
    suspend fun productListPagingData(@Query("page") page: Int = 1): PagedResponse<DummyPagerModel>
}