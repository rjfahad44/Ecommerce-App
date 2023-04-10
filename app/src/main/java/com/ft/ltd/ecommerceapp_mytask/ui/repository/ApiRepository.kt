package com.ft.ltd.ecommerceapp_mytask.ui.repository

import com.haroldadmin.cnradapter.NetworkResponse
import com.ft.ltd.ecommerceapp_mytask.data.apis.ApiService
import com.ft.ltd.ecommerceapp_mytask.data.apis.Response
import com.ft.ltd.ecommerceapp_mytask.data.model.Categories
import com.ft.ltd.ecommerceapp_mytask.data.model.ErrorResponse
import com.ft.ltd.ecommerceapp_mytask.data.model.Products
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getProductCategories(): Response<Categories, ErrorResponse> {
        when (val response = apiService.getProductCategories()) {
            is NetworkResponse.Success -> {
                val data = response.body
                return Response.success(data)
            }
            is NetworkResponse.Error -> {
                val error = response.body
                val errorResponse = ErrorResponse()
                if (error != null) {
                    errorResponse.code = error.code
                    errorResponse.message = error.message
                } else {
                    errorResponse.message = response.error?.localizedMessage.orEmpty()
                }
                return Response.error(null, errorResponse)
            }
        }
    }

    suspend fun getProducts(): Response<Products, ErrorResponse> {
        when (val response = apiService.getProducts()) {
            is NetworkResponse.Success -> {
                val data = response.body
                return Response.success(data)
            }
            is NetworkResponse.Error -> {
                val error = response.body
                val errorResponse = ErrorResponse()
                if (error != null) {
                    errorResponse.code = error.code
                    errorResponse.message = error.message
                } else {
                    errorResponse.message = response.error?.localizedMessage.orEmpty()
                }
                return Response.error(null, errorResponse)
            }
        }
    }
}