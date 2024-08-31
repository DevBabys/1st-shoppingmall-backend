package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.DTO.Authentication.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.Order.OrderRequest
import com.devbabys.shoppingmall.DTO.Order.OrderResponse
import com.devbabys.shoppingmall.DTO.Order.OrderStateRequest
import com.devbabys.shoppingmall.Service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
class OrderController @Autowired constructor(
    private val orderService: OrderService
) {
    @GetMapping("order/list")
    fun getOrderListAll(@RequestHeader("Authorization") authResponse: AuthenticationResponse,
                     pageable: Pageable)
    : ResponseEntity<Map<String, Any>> {
        val (response, description, value) = orderService.getOrderListAll(authResponse, pageable)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    @GetMapping("order/listByPeriod")
    fun getOrderListByPeriod(@RequestHeader("Authorization") authResponse: AuthenticationResponse,
                             @RequestParam("startDate", required=false) startDate: LocalDate?,
                             @RequestParam("endDate", required=false) endDate: LocalDate?,
                             pageable: Pageable)
            : ResponseEntity<Map<String, Any>> {
        val (response, description, value) = orderService.getOrderListByPeriod(authResponse, startDate, endDate, pageable)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

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
                    @RequestBody orderResponse: OrderResponse)
    : ResponseEntity<Map<String, String>> {
        val (response, description, value) = orderService.completeOrder(orderResponse)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    @PostMapping("order/fail")
    fun failOrder(@RequestHeader("Authorization") authResponse: AuthenticationResponse,
                    @RequestBody orderResponse: OrderResponse)
    : ResponseEntity<Map<String, String>> {
        val (response, description, value) = orderService.failOrder(orderResponse)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    @PostMapping("order/changeState")
    fun changeOrderState(@RequestHeader("Authorization") authResponse: AuthenticationResponse,
                         @RequestBody orderStateRequest: OrderStateRequest)
            : ResponseEntity<Map<String, String>> {
        val (response, description, value) = orderService.changeOrderState(orderStateRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }
}