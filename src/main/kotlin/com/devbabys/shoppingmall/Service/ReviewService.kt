package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.DTO.Authentication.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.Review.ReviewRequest
import com.devbabys.shoppingmall.Entity.ProductReview
import com.devbabys.shoppingmall.Repository.OrderDetailRepo
import com.devbabys.shoppingmall.Repository.ProductRepo
import com.devbabys.shoppingmall.Repository.ProductReviewRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ReviewService @Autowired constructor(
    private val reviewRepo: ProductReviewRepo,
    private val orderDetailRepo: OrderDetailRepo,
    private val productRepo: ProductRepo,
    private val jwtService: JwtService,
){
    fun addReview(authResponse: AuthenticationResponse,
                  reviewRequest: ReviewRequest): Triple<String, String, String> {
        try {
            val user = jwtService.extractedUserInfo(authResponse)
            val product = productRepo.findById(reviewRequest.productId).orElse(null)
                ?: return Triple("fail", "addReview", "product not exist")

            val isProductOrder = orderDetailRepo.findByUserIdAndProductId(user, product)

            // 주문한 제품에 대해서만 리뷰 추가
            if(isProductOrder == null){
                return Triple("fail", "addReview", "The item is not in your order history")
            }

            val review = ProductReview(product = product, user = user, rating = reviewRequest.rating, comment = reviewRequest.comment)
            val saveReview = reviewRepo.save(review)

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

            // OrderDetail에서 주문한 제품 리스트 조회
            val productList = orderDetailRepo.findDistinctProductIdsByUserId(user)
            println("일단 여기")
           val reviewList = reviewRepo.findByUserIdAndProductIdIn(user, productList,customPage)

            var result: List<Any> = mutableListOf()
            reviewList.forEach {
                val reviewDetails = mapOf(
                    "reviewId" to it.reviewId,
                    "productId" to it.productId,
                    "rating" to it.rating,
                    "image" to it.image,
                    "comment" to it.comment,
                    "createAt" to it.createAt,
                    "reviewStatus" to it.reviewStatus,
                    "productName" to it.name,
                )
                result = result + reviewDetails
            }

            val resultMap = mapOf(
                "total" to productList.size, // 주문한 제품 수 만큼 작성해야 할 + 작성할 리뷰 수
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
            val productReviewList = reviewRepo.findByProduct(product, customPage)

            var result: List<Any> = mutableListOf()
            productReviewList.forEach {
                val reviewDetails = mapOf(
                    "reviewId" to it.reviewId,
                    "productId" to it.product.productId,
                    "rating" to it.rating,
                    "userName" to it.user.username,
                    "comment" to it.comment,
                    "createAt" to it.createAt,
                )
                result = result + reviewDetails
            }

            val resultMap = mapOf(
                "total" to reviewRepo.countByProduct(product),
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
                if (review.user.userId != user.userId) {
                    return Triple("fail", "updateReview", "Not a user-generated review")
                }

                val reviewInfo = reviewRepo.save(
                    ProductReview(
                        reviewId = review.reviewId,
                        product = review.product,
                        user = review.user,
                        rating = reviewRequest.rating,
                        comment = reviewRequest.comment,
                        createAt = review.createAt,
                    )
                )

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

                if (review.user.userId != user.userId) {
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