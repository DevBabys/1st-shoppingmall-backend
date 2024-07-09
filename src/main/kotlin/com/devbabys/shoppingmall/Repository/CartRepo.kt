package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Entity.Cart
import com.devbabys.shoppingmall.Entity.Product
import com.devbabys.shoppingmall.Entity.User
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CartRepo: JpaRepository<Cart, Long> {
    override fun findById(cartId: Long): Optional<Cart>

    fun findByUserId(userId: User): Optional<List<Cart>>

    fun findByProductId(productId: Product): Optional<Cart>

    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.quantity = :quantity WHERE c.id = :cartId")
    fun updateQuantity(@Param("cartId") cartId: Long, @Param("quantity") quantity: Int): Int
}