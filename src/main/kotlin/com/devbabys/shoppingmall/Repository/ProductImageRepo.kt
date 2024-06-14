package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Model.Product
import com.devbabys.shoppingmall.Model.ProductImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductImageRepo: JpaRepository<ProductImage, Long> {
    @Query("SELECT p FROM ProductImage p WHERE p.isPrimary = true AND p.productId = :productId")
    fun findByProductId(productId: Product): ProductImage?
}