package com.devbabys.shoppingmall.DTO.User

data class FindUserResponse (
    val email: String,
    // code : 비밀번호 임시 코드 확인 로직 사용에만 사용
    // val code: String,
    val username: String,
    val password: String
)