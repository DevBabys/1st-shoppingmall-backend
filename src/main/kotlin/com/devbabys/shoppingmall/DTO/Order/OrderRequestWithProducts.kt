package com.devbabys.shoppingmall.DTO.Order

data class OrderRequest (
    val paymentId: String? = null,
    val product: List<ProductDetailRequest>
)

data class ProductDetailRequest(
    val productId: Long,
    val quantity: Int
)