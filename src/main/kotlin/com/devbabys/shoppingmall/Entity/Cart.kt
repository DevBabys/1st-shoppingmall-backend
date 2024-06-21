package com.devbabys.shoppingmall.Entity

import jakarta.persistence.*

@Entity
@Table(name = "cart")
data class Cart(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var cartId: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var userId: User,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    var product: Product,

    @Column(nullable = false)
    var quantity: Int
)