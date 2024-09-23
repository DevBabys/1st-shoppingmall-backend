package com.devbabys.shoppingmall.Config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UriConfig {
    // 베이스 URL
    private val BASEURL = "https://project1.babychat.xyz/"

    // 관리자만 접근할 수 있는 URL
    private val adminAllowedUrls = arrayOf(
        "/product/category/**", // 카테고리 관련
        "/order/changeState" // 결제 상태 변경
    )

    // 판매자와 관리자가 접근할 수 있는 URL
    private val sellerAllowedUrls = arrayOf(
        "/test/test/test"
    )

    // AuthorityInterceptor 제외 URL
    private val exclusiveUrls = arrayOf(
    "/product/category/list", // 상품 카테고리 관련
    "/product/list/**" // 상품 리스트 관련
    )

    @Bean
    fun getAdminAllowedUrls(): Array<String> {
        return adminAllowedUrls
    }

    @Bean
    fun getSellerAllowedUrls(): Array<String> {
        return sellerAllowedUrls
    }

    @Bean
    fun getExclusiveUrls(): Array<String> {
        return exclusiveUrls
    }

    // 관리자와 해당 유저만 접근할 수 있는 URL
    private val certUserAllowedUrls = arrayOf(
        "/cart/update", "/cart/delete",
        "/order/list/*"
    )

    // CertainUserAccessInterceptor 제외 URL
    private val certUserExclusiveUrls = arrayOf(
        "/test/test/test"
    )

    @Bean
    fun getCertUserAllowedUrls(): Array<String> {
        return certUserAllowedUrls
    }

    @Bean
    fun getCertUserExclusiveUrls(): Array<String> {
        return certUserExclusiveUrls
    }
}