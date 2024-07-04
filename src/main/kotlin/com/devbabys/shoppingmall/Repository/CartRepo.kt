package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Entity.Cart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CartRepo: JpaRepository<Cart, Long> {
}