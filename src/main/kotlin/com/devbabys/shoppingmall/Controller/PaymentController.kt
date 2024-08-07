package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.DTO.Authentication.AuthenticationResponse
import com.devbabys.shoppingmall.Service.PaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class PaymentController @Autowired constructor(
    private val paymentService: PaymentService
){
    // 결제
    @PostMapping("payment/toss/order")
    fun tossOrder(//@RequestHeader("Authorization") authRequest: AuthenticationResponse
    ) : ResponseEntity<Map<String, String>> {
        println("test")
        paymentService.tossOrder(1, 500)
        val result = mapOf("result" to "success")
        println("test2")
        return ResponseEntity.ok(result)
    }

    @GetMapping("payment/toss/success")
    fun tossSuccess(): String {
        return "test"
    }
}