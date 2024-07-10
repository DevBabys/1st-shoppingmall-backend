package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.DTO.Authentication.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.Review.ReviewRequest
import com.devbabys.shoppingmall.Entity.ProductReview
import com.devbabys.shoppingmall.Repository.ProductOrderRepo
import com.devbabys.shoppingmall.Repository.ProductRepo
import com.devbabys.shoppingmall.Repository.ProductReviewRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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
            // TODO: IMAGE SAVE

            return Triple("success", "addReview", saveReview.reviewId.toString())
        } catch (e: Exception) {
            return Triple("fail", "addReview", "program error : $e")
        }
    }

    fun getReviewList(authResponse: AuthenticationResponse, pageable: Pageable): Triple<String, String, Any> {
        try {
            var customPage = pageable
            if (pageable.pageNumber > 0) {
                customPage = PageRequest.of(pageable.pageNumber - 1, pageable.pageSize)
            }

            val user = jwtService.extractedUserInfo(authResponse)
            val reviewList = reviewRepo.findByUserId(user, customPage)

            var result: List<Any> = mutableListOf()
            reviewList.forEach {
                // TODO: IMAGE GET, LIKE GET
                val reviewDetails = mapOf(
                    "reviewId" to it.reviewId,
                    "productId" to it.productId.productId,
                    "rating" to it.rating,
                    "userName" to it.userId.username,
                    "comment" to it.comment,
                    "createAt" to it.createAt,
                )
                result = result + reviewDetails
            }

            val resultMap = mapOf(
                "total" to reviewList.size,
                "data" to result
            )
            return Triple("success", "getReviewList", resultMap)
        } catch (e: Exception) {
            return Triple("fail", "getReviewList", "error : $e")
        }
    }

    fun getProductReviewList(productId: Long, pageable: Pageable): Triple<String, String, Any> {
        try {
            var customPage = pageable
            if (pageable.pageNumber > 0) {
                customPage = PageRequest.of(pageable.pageNumber, pageable.pageSize )
            }

            val product = productRepo.findById(productId).orElse(null)
                ?: return Triple("fail", "getProductReviewList", "product not found")
            val productReviewList = reviewRepo.findByProductId(product, customPage)

            var result: List<Any> = mutableListOf()
            productReviewList.forEach {
                // TODO: IMAGE GET, LIKE GET
                val reviewDetails = mapOf(
                    "reviewId" to it.reviewId,
                    "productId" to it.productId.productId,
                    "rating" to it.rating,
                    "userName" to it.userId.username,
                    "comment" to it.comment,
                    "createAt" to it.createAt,
                )
                result = result + reviewDetails
            }

            val resultMap = mapOf(
                "total" to productReviewList.size,
                "data" to result
            )
            return Triple("success", "getProductReviewList", resultMap)
        } catch (e: Exception) {
            return Triple("fail", "getProductReviewList", "error : $e")
        }
    }

    fun updateReview(authResponse: AuthenticationResponse, reviewRequest: ReviewRequest): Triple<String, String, String> {
        try {
            if (reviewRequest.reviewId == null) {
                return Triple("fail", "updateReview", "miss match json type data in body")
            } else {
                val review = reviewRepo.findById(reviewRequest.reviewId).orElse(null)
                ?: return Triple("fail", "updateReview", "review not found")

                val user = jwtService.extractedUserInfo(authResponse)
                if (review.userId.userId != user.userId) {
                    return Triple("fail", "updateReview", "Not a user-generated review")
                }

                val reviewInfo = reviewRepo.save(
                    ProductReview(
                        reviewId = review.reviewId,
                        productId = review.productId,
                        userId = review.userId,
                        rating = reviewRequest.rating,
                        comment = reviewRequest.comment,
                        likes = review.likes,
                        createAt = review.createAt,
                    )
                )

                // TODO: IMAGE UPDATE OR DELETE


                return Triple("success", "updateReview", "")
            }
        } catch (e: Exception) {
            return Triple("fail", "updateReview", "error : $e")
        }
    }

    fun deleteReview(authResponse: AuthenticationResponse, reviewRequest: ReviewRequest): Triple<String, String, String> {
        try {
            if (reviewRequest.reviewId == null) {
                return Triple("fail", "deleteReview", "miss match json type data")
            } else {
                val review = reviewRepo.findById(reviewRequest.reviewId).orElse(null)
                val user = jwtService.extractedUserInfo(authResponse)

                if (review.userId.userId != user.userId) {
                    return Triple("fail", "deleteReview", "Not a user-generated review")
                }

                reviewRepo.delete(review)
                return Triple("success", "deleteReview", reviewRequest.reviewId.toString())
            }
        } catch (e:Exception) {
            return Triple("fail", "deleteReview", "error : $e")
        }
    }
}