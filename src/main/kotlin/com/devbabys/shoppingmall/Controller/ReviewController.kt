package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.DTO.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.Review.ReviewRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class ReviewController {
    /**
     * 리뷰 등록
     */
    @PostMapping("review")
    fun saveReview(@RequestHeader("Authorization") authRequest: AuthenticationResponse, @RequestBody reviewRequest: ReviewRequest) {

    }

}