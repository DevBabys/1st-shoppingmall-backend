package com.devbabys.shoppingmall.Model

import jakarta.persistence.*

@Entity
@Table(name = "cart")
data class Cart(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val cartId: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val userId: User,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,

    @Column(nullable = false)
    val quantity: Int
)