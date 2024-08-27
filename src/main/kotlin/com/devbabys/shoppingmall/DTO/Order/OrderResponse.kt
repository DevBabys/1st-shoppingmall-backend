package com.devbabys.shoppingmall.DTO.Order

data class OrderResponse (
    val paymentId: String, // 결제 번호 (devbabys_shop_payment_ + Order ID 로 구성)
    val txId: String?, // 카드 결제 시 반환받는 결제 승인 번호
    val totalAmount: Long? // 결제 총액
)