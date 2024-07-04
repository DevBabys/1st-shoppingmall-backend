package com.devbabys.shoppingmall.DTO.Cart

data class CartRequest (
    val cartId: Long? = null,
    val userId: Long,
    val productId: Long? = null,
    val quantity: Int? = null
)