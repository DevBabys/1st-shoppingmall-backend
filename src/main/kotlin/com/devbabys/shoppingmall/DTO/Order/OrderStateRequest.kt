package com.devbabys.shoppingmall.DTO.Order

data class OrderStateRequest (
    val orderId: Long,
    val orderState: Int
)