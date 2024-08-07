package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Entity.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepo: JpaRepository<Order, Long> {
}