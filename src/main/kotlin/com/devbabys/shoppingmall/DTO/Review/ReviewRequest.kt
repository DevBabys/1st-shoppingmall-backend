package com.devbabys.shoppingmall.DTO.Review

data class ReviewRequest (
    val reviewId: Long? = null,
    val productId: Long,
    val rating: Int,
    val comment: String,
)