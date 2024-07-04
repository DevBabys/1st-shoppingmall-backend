package com.devbabys.shoppingmall.Entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "cart")
data class Cart(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var cartId: Long = 0, // 카트 식별 ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var userId: User, // 유저 식별 ID

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    var productId: Product, // 상품 식별 ID

    @Column(nullable = false)
    var quantity: Int, // 수량

    @Column(name = "added_at", nullable = false)
    var addedAt: LocalDateTime = LocalDateTime.now() // 카트 추가일자
)