package com.devbabys.shoppingmall.Entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "product_order")
data class ProductOrder(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var orderId: Long = 0, // 주문번호

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var userId: User, // 구매자

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    var productId: Product, // 상품번호

    @Column(nullable = false)
    var orderDate: Date, // 주문일자

    @Column(nullable = false)
    var orderState: String, // 주문상태
    
    var trackingNo: Int, // 운송장번호

    @Column(nullable = false)
    var quantity: Int, // 구매 수량

    @Column(nullable = false)
    var totalPrice: Double // 총 가격
)