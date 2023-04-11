package com.ft.ltd.ecommerceapp_mytask.ui.paging_adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ft.ltd.ecommerceapp_mytask.data.apis.ApiService

class ProductListPagingSource(
    private val client: ApiService,
) : PagingSource<Int, DummyPagerModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DummyPagerModel> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = client.productListPagingData(nextPageNumber)
            LoadResult.Page(
                    data = response.data,
                    prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                    nextKey = if (nextPageNumber == response.lastPage) null else nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DummyPagerModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}