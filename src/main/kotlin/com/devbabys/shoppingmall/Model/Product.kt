package com.devbabys.shoppingmall.Model

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter
import java.math.BigDecimal
import java.time.LocalDateTime

@Getter
@Setter
@Entity
@Table(name = "products")
class Product (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var productId : Long = 0, // 상품 식별 ID

    @Column(nullable = false)
    var name: String, // 상품 이름

    @Column(columnDefinition = "TEXT")
    var description: String? = null, // 상품 설명

    @Column(nullable = false)
    var price: BigDecimal, // 상품 가격

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    val categoryId: ProductCategory,  // 상품 카테고리

    @Column(nullable = false)
    var quantity: Int, // 상품 수량

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(), // 등록일자

    @Column(nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now() // 수정일자
)