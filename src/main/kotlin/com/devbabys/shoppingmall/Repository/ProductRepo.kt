package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.lang.Nullable
import org.springframework.stereotype.Repository

@Repository
interface ProductRepo: JpaRepository<Product, Long> {
    @Nullable
    fun findByUserId(userId: Long): Product
}