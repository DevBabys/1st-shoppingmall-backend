package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Entity.Product
import com.devbabys.shoppingmall.Entity.ProductImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductImageRepo: JpaRepository<ProductImage, Long> {
    @Query("SELECT p FROM ProductImage p WHERE p.isPrimary = true AND p.productId = :productId")
    fun findByProductIdAndIsPrimary(productId: Product): ProductImage?

    fun findByProductId(productId: Product): List<ProductImage>
}