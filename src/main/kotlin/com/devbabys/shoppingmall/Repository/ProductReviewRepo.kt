package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Entity.ProductReview
import org.springframework.data.jpa.repository.JpaRepository

interface ProductReviewRepo: JpaRepository<ProductReview, Long> {
}