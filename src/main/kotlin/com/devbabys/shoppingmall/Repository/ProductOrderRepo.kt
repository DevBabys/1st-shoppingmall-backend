package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Entity.ProductOrder
import org.springframework.data.jpa.repository.JpaRepository

interface ProductOrderRepo: JpaRepository<ProductOrder, Long> {
    fun findByUserIdAnAndProductId(user_id: Long, product_id: Long): ProductOrder?
}