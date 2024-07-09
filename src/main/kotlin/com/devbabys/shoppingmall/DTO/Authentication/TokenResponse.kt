package com.devbabys.shoppingmall.DTO.Authentication

/* TODO : 리프레시 토큰 개발 시 사용 할 데이터 모델, 현재 보류 */
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)