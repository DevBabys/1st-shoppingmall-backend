package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.DTO.Review.UserReview
import com.devbabys.shoppingmall.Entity.Product
import com.devbabys.shoppingmall.Entity.ProductReview
import com.devbabys.shoppingmall.Entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductReviewRepo: JpaRepository<ProductReview, Long> {
    @Query("""
        SELECT new  com.devbabys.shoppingmall.DTO.Review.UserReview(
            r.reviewId, 
            p.productId,
            p.name,
            pi.url,
            CASE WHEN r.reviewId IS NOT NULL THEN true else false END,
            COALESCE(r.comment, ''),
            COALESCE(r.rating, 0),
            COALESCE(r.createAt, null)
        )
        FROM Product p
        LEFT JOIN ProductReview r  ON r.product = p AND r.user = :user
        LEFT JOIN ProductImage pi ON pi.productId = p AND pi.isPrimary = true
        WHERE p.productId IN :products
    """)
    fun findByUserIdAndProductIdIn(user: User, products: List<Long>, pageable: Pageable): Page<UserReview>
    fun findByProduct(product: Product, pageable: Pageable): Page<ProductReview>
    fun countByProduct(product: Product): Long
}