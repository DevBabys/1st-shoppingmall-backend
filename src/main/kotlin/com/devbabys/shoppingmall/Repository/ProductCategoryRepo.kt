package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Entity.ProductCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductCategoryRepo: JpaRepository<ProductCategory, Long> {
    fun findByName(name: String): ProductCategory?
}