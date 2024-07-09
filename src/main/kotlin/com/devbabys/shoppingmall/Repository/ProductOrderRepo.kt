package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Entity.Product
import com.devbabys.shoppingmall.Entity.ProductOrder
import com.devbabys.shoppingmall.Entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ProductOrderRepo: JpaRepository<ProductOrder, Long> {
    fun findByUserIdAndProductId(userId: User, productId: Product): ProductOrder?
}