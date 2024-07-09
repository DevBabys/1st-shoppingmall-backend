package com.devbabys.shoppingmall.Config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UriConfig {
    // 베이스 URL
    private val BASEURL = "https://project1.babychat.xyz/"

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

    // 관리자와 해당 판매자만 접근할 수 있는 URL
    private val certSellerAllowedUrls = arrayOf(
        "/cart/update", "/cart/delete"
    )

    @Bean
    fun getCertSellerAllowedUrls(): Array<String> {
        return certSellerAllowedUrls
    }
}