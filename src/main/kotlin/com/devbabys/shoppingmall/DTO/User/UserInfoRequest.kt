package com.devbabys.shoppingmall.DTO.User

data class UserInfoRequest (
    val password: String,
    val username: String,
    val phoneNumber: String,
    val zipCode: String,
    val address: String,
    val detailAddress: String
)