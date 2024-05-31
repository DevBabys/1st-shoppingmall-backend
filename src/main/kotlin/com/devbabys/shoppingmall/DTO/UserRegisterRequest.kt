package com.devbabys.shoppingmall.DTO

data class UserRegisterRequest (
    val email: String,
    val password: String,
    val username: String
)