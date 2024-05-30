package com.devbabys.shoppingmall.Model

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter
import java.time.LocalDateTime

@Getter
@Setter
@Entity
@Table(name = "product_reviews")
class ProductReviews (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var reviewId : Long = 0, // 리뷰 식별 ID

    @ManyToOne
    @JoinColumn(name = "product_id")
    var productId : Product, // 참조되는 상품 ID

    @ManyToOne
    @JoinColumn(name = "user_id")
    var userId: User, // 참조되는 유저 ID

    @Column(nullable = false)
    var rating: Int, // 리뷰 평점

    @Column(columnDefinition = "TEXT")
    var comment: String? = null, // 리뷰 내용

    @Column(nullable = false, updatable = false)
    var createAt: LocalDateTime = LocalDateTime.now()
)