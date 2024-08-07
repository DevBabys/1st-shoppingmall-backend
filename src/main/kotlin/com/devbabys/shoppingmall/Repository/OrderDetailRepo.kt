package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Entity.OrderDetail
import com.devbabys.shoppingmall.Entity.Product
import com.devbabys.shoppingmall.Entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderDetailRepo: JpaRepository<OrderDetail, Long> {
    fun findByUserIdAndProductId(userId: User, productId: Product): OrderDetail?
}