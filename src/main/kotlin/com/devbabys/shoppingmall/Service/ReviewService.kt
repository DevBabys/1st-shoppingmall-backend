package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.DTO.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.Review.ReviewRequest
import com.devbabys.shoppingmall.Entity.ProductReview
import com.devbabys.shoppingmall.Repository.ProductOrderRepo
import com.devbabys.shoppingmall.Repository.ProductRepo
import com.devbabys.shoppingmall.Repository.ProductReviewRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReviewService @Autowired constructor(
    private val reviewRepo: ProductReviewRepo,
    private val orderRepo: ProductOrderRepo,
    private val productRepo: ProductRepo,
    private val jwtService: JwtService,
){
    fun addReview(authResponse: AuthenticationResponse,
                  reviewRequest: ReviewRequest): Triple<String, String, String> {
        try {
            val user = jwtService.extractedUserInfo(authResponse)
            val product = productRepo.findById(reviewRequest.productId).orElse(null)
                ?: return Triple("fail", "addReview", "product not exist")

            val isProductOrder = orderRepo.findByUserIdAndProductId(user, product)

            // 주문한 제품에 대해서만 리뷰 추가
            if(isProductOrder == null){
                return Triple("fail", "addReview", "The item is not in your order history")
            }

            val review = ProductReview(productId = product, userId = user, rating = reviewRequest.rating, comment = reviewRequest.comment, likes = 0)
            val saveReview = reviewRepo.save(review)
            return Triple("success", "addReview", saveReview.reviewId.toString())
        } catch (e: Exception) {
            return Triple("fail", "addReview", "program error : $e")
        }
    }
}