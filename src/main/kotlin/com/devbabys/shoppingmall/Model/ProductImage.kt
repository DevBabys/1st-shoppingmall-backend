package com.devbabys.shoppingmall.Model

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter

@Getter
@Setter
@Entity
@Table(name = "product_image")
data class ProductImage (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var imageId : Long = 0, // 이미지 식별 ID

    @ManyToOne
    @JoinColumn(name = "product_id")
    var productId : Product, // 참조되는 상품 ID

    @Column(nullable = false)
    var url: String, // 이미지 URL

    @Column(nullable = false)
    var isPrimary: Boolean = false // 상품 대표 이미지 여부
)