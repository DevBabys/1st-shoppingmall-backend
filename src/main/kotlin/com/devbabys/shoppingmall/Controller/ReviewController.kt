package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.DTO.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.Review.ReviewRequest
import com.devbabys.shoppingmall.Service.ReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

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

}