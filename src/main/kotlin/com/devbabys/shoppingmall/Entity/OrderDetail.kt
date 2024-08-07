package com.devbabys.shoppingmall.Entity

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter

@Entity
@Setter
@Getter
@Table(name = "orders_detail")
data class OrderDetail (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0, // 인덱스

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    var orderId: Order, // 주문 번호

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var userId: User, // 구매자

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    var productId: Product, // 상품 번호

    @Column(nullable = false)
    var quantity: Int, // 구매 수량
)