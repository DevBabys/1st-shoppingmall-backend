package com.devbabys.shoppingmall.Model

data class AuthenticationRequest(
    val email: String,
    val password: String
)

data class ProductRequest(
    val title: String,
    val content: String
)