package com.devbabys.shoppingmall.DTO.Order

import java.time.LocalDate

data class OrderHistoryRequest (
    val userId: Long,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
)