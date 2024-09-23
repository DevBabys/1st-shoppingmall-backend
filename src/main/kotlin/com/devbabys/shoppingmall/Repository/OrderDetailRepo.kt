package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Entity.Order
import com.devbabys.shoppingmall.Entity.OrderDetail
import com.devbabys.shoppingmall.Entity.Product
import com.devbabys.shoppingmall.Entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface OrderDetailRepo: JpaRepository<OrderDetail, Long> {
    fun findFirstByOrderId(orderId: Order): OrderDetail?
    fun countByOrderId(orderId: Order): Long
    @Query("""
       SELECT DISTINCT o.productId.productId 
       FROM OrderDetail o
       WHERE o.userId = :userId
    """)
    fun findDistinctProductIdsByUserId(userId: User): List<Long>
    fun findFirstByUserIdAndProductId(userId: User, productId: Product): OrderDetail?
}