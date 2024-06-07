package com.devbabys.shoppingmall.DTO.User

data class FindUserResponse (
    val email: String,
    val code: String,
    val password: String
)