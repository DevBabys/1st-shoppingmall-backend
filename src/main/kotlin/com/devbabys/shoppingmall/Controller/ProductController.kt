package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.Model.Product
import com.devbabys.shoppingmall.Security.JwtUtil
import com.devbabys.shoppingmall.Service.ProductService
import com.devbabys.shoppingmall.DTO.ProductRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class ProductController(
    @Autowired private val productService: ProductService,
    @Autowired private val jwtUtil: JwtUtil
) {

    @GetMapping("product/list")
    fun getProduct(model: Model) : String {
        return "product/product_list"
    }

    @GetMapping("product/{num}")
    @ResponseBody
    fun product(model: Model, @PathVariable num : Int) : String {
        println("num:\t${num}")
        return "ok"
    }

//    @PostMapping("product/create")
//    @ResponseBody
//    fun createPost(
//        @RequestHeader("Authorization") authorizationHeader: String,
//        @RequestBody productRequest: ProductRequest
//    ): Product {
//        val token = authorizationHeader.substring(7) // "Bearer " 부분 제거
//        val userId = jwtUtil.extractedUserId(token)
//        return productService.createProduct(userId, productRequest.title, productRequest.content)
//    }
}