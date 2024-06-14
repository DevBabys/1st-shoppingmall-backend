package com.devbabys.shoppingmall.DTO.Product

data class ProductCategoryRequest (
    val categoryId: Long ? = null,
    val name: String,
    val description: String
)