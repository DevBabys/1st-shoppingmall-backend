package com.devbabys.shoppingmall.Config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UriConfig {
    // 관리자만 접근할 수 있는 URL
    private val adminAllowedUrls = arrayOf(
        "/product/category/**" // 카테고리 관련
    )

    // 판매자와 관리자가 접근할 수 있는 URL
    private val sellerAllowedUrls = arrayOf(
        "/test/test/test"
        //"/product/**"
    )

    @Bean
    fun getAdminAllowedUrls(): Array<String> {
        return adminAllowedUrls
    }

    @Bean
    fun getSellerAllowedUrls(): Array<String> {
        return sellerAllowedUrls
    }
}