package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.DTO.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.Cart.CartRequest
import com.devbabys.shoppingmall.Service.CartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class CartController @Autowired constructor(
    private val cartService: CartService
){

    // 카트 추가
    @PostMapping("cart/add")
    fun postLogin(@RequestHeader("Authorization") authRequest: AuthenticationResponse,
                  @RequestBody cartRequest: CartRequest)
    : ResponseEntity<Map<String, String>> {
        val (response, description, value) = cartService.addCart(authRequest, cartRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }
}