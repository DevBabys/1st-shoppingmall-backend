package com.devbabys.shoppingmall.DTO.User

data class UserRegisterRequest (
    val email: String,
    val password: String,
    val username: String
)