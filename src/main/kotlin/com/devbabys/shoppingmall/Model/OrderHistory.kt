package com.devbabys.shoppingmall.Model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "order_history")
data class OrderHistory(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val orderHistoryId: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val userId: User,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    val productId: Product,

    @Column(nullable = false)
    val orderDate: Date,

    @Column(nullable = false)
    val quantity: Int,

    @Column(nullable = false)
    val totalPrice: Double
)