package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.DTO.Authentication.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.Cart.CartRequest
import com.devbabys.shoppingmall.Service.CartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class CartController @Autowired constructor(
    private val cartService: CartService
){

    // 카트 추가
    @PostMapping("cart/add")
    fun addCart(@RequestHeader("Authorization") authRequest: AuthenticationResponse,
                @RequestBody cartRequest: CartRequest)
    : ResponseEntity<Map<String, String>> {
        val (response, description, value) = cartService.addCart(authRequest, cartRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    // 카트 조회
    @GetMapping("cart/list")
    fun getCartList(@RequestHeader("Authorization") authRequest: AuthenticationResponse)
            : ResponseEntity<Map<String, Any>> {
        val (response, description, value) = cartService.getCartList(authRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    // 카트 업데이트
    @PutMapping("cart/update")
    fun updateCart(@RequestHeader("Authorization") authRequest: AuthenticationResponse,
                   @RequestBody cartRequest: CartRequest)
    : ResponseEntity<Map<String, String>> {
        val (response, description, value) = cartService.updateCart(authRequest, cartRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    // 카트 삭제
    @DeleteMapping("cart/delete")
    fun deleteCart(@RequestHeader("Authorization") authRequest: AuthenticationResponse,
                   @RequestBody cartRequest: CartRequest)
    : ResponseEntity<Map<String, String>> {
        val (response, description, value) = cartService.deleteCart(authRequest, cartRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }
}