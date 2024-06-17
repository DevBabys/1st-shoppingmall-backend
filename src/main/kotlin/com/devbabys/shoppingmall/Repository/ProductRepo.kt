package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Model.Product
import com.devbabys.shoppingmall.Model.ProductCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepo: JpaRepository<Product, Long> {
    override fun findById(productId: Long): Optional<Product>
    override fun findAll(pageable: Pageable): Page<Product>
    fun findByCategoryId(categoryId: ProductCategory, pageable: Pageable): Page<Product>
}