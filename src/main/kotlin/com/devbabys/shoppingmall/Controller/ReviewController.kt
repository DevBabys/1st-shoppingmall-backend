package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.DTO.Authentication.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.Review.ReviewRequest
import com.devbabys.shoppingmall.Service.ReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ReviewController @Autowired constructor(
    private val reviewService: ReviewService
){
    /**
     * 리뷰 등록
     */
    @PostMapping("review/add")
    fun saveReview(@RequestHeader("Authorization") authRequest: AuthenticationResponse, @RequestBody reviewRequest: ReviewRequest) :ResponseEntity<Map<String, String>> {
        val (response, description, value) = reviewService.addReview(authRequest, reviewRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)
        return ResponseEntity.ok(result)
    }

    /**
     * 사용자별 리뷰 리스트
     */
    @GetMapping("review/list")
    fun getReviews(@RequestHeader("Authorization") authRequest: AuthenticationResponse, pageable: Pageable): ResponseEntity<Map<String, Any>> {
        val (response, description, value) = reviewService.getReviewList(authRequest, pageable)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    /**
     * 상품별 리뷰 리스트
     */
    @GetMapping("review/list/{productId}")
    fun getProductReviews(@PathVariable productId: Long, pageable: Pageable) : ResponseEntity<Map<String, Any>> {
        val (response, description, value) = reviewService.getProductReviewList(productId, pageable)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    /**
     * 리뷰 수정
     */
    @PutMapping("review/update")
    fun updateReview(@RequestHeader("Authorization") authRequest: AuthenticationResponse, @RequestBody reviewRequest: ReviewRequest) : ResponseEntity<Map<String, String>> {
        val (response, description, value) = reviewService.updateReview(authRequest, reviewRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("review/delete")
    fun deleteReview(@RequestHeader("Authorization") authRequest: AuthenticationResponse, reviewRequest: ReviewRequest) : ResponseEntity<Map<String, String>> {
        val (response, description, value) = reviewService.deleteReview(authRequest, reviewRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }
}