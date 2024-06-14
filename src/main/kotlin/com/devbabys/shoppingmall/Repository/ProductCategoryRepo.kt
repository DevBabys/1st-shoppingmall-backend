package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Model.Product
import com.devbabys.shoppingmall.Model.ProductCategory
import com.devbabys.shoppingmall.Model.ProductImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.lang.Nullable
import org.springframework.stereotype.Repository

@Repository
interface ProductCategoryRepo: JpaRepository<ProductCategory, Long> {
    fun findByName(name: String): ProductCategory?
}