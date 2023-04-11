package com.ft.ltd.ecommerceapp_mytask.ui.paging_adapter

data class DummyPagerModel(
    var id: Int,
    var totalPage: Int,
    var currentPage: Int,
    var title: String,
    var img: String,
    var status: Int,
    var `data`: ArrayList<DummyData>
)

data class DummyData(
    val id: Int,
    var name: String
)
