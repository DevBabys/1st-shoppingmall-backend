package com.devbabys.shoppingmall.Entity

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter
import java.time.LocalDateTime

@Getter
@Setter
@Entity
@Table(name = "product")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var productId: Long = 0, // 상품 식별 ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var userId: User, // 작성 그룹

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    var categoryId: ProductCategory,  // 상품 카테고리

    @Column(nullable = false)
    var name: String, // 상품 이름

    @Column(columnDefinition = "TEXT")
    var description: String? = null, // 상품 설명

    @Column(nullable = false)
    var price: Long, // 상품 가격

    @Column(nullable = false)
    var quantity: Int, // 상품 수량

    @Column(nullable = false)
    var isPublic: Boolean = true, // 공개 여부

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(), // 등록일자

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now() // 수정일자
)