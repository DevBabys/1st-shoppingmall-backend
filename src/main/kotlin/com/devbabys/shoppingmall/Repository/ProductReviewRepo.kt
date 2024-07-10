package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Entity.Product
import com.devbabys.shoppingmall.Entity.ProductReview
import com.devbabys.shoppingmall.Entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ProductReviewRepo: JpaRepository<ProductReview, Long> {
    fun findByUserId(userId: User, pageable: Pageable): Page<ProductReview>
    fun findByProductId(productId: Product, pageable: Pageable): Page<ProductReview>
}