package com.ft.ltd.ecommerceapp_mytask.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.Resource
import com.ft.ltd.ecommerceapp_mytask.data.apis.Response
import com.ft.ltd.ecommerceapp_mytask.data.model.Categories
import com.ft.ltd.ecommerceapp_mytask.data.model.ErrorResponse
import com.ft.ltd.ecommerceapp_mytask.data.model.Products
import com.ft.ltd.ecommerceapp_mytask.ui.repository.ApiRepository
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiViewModel @Inject constructor(private val apiRepository: ApiRepository): ViewModel() {

    private var _getProductCategories = MutableSharedFlow<Response<Categories, ErrorResponse>>()
    var getProductCategories: SharedFlow<Response<Categories, ErrorResponse>> = _getProductCategories

    fun getProductCategories() = viewModelScope.launch {
        _getProductCategories.emit(Response.loading(null))
        _getProductCategories.emit(apiRepository.getProductCategories())
    }


    private var _getProducts = MutableSharedFlow<Response<Products, ErrorResponse>>()
    var getProducts: SharedFlow<Response<Products, ErrorResponse>> = _getProducts

    fun getProducts() = viewModelScope.launch {
//        _getProducts.emit(Response.loading(null))
        _getProducts.emit(apiRepository.getProducts())
    }

}