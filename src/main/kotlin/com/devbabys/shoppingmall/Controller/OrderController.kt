package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.DTO.Authentication.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.Order.OrderRequest
import com.devbabys.shoppingmall.Service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController @Autowired constructor(
    private val orderService: OrderService
) {
    @PostMapping("order/add")
    fun addOrder(@RequestHeader("Authorization") authResponse: AuthenticationResponse,
                 @RequestBody orderRequest: OrderRequest)
    : ResponseEntity<Map<String, Any>> {
        val (response, description, value) = orderService.addOrder(authResponse, orderRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    @PostMapping("order/complete")
    fun completeOrder(@RequestHeader("Authorization") authResponse: AuthenticationResponse,
                    @RequestBody orderRequest: OrderRequest)
    : ResponseEntity<Map<String, String>> {
        val (response, description, value) = orderService.completeOrder(orderRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

//    @PostMapping("order/update")
//    fun updateOrder(@RequestHeader("Authorization") authResponse: AuthenticationResponse,
//                    @RequestBody orderRequest: OrderRequest)
//    : ResponseEntity<Map<String, String>> {
//
//    }
}