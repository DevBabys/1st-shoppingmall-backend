package com.devbabys.shoppingmall.DTO.Review

import java.time.LocalDateTime

data class UserReview(
    val reviewId: Long? = null,
    val productId: Long,
    val name: String,
    val image: String,
    val reviewStatus: Boolean,
    val comment: String,
    val rating: Int,
    val createAt: LocalDateTime?,
)
