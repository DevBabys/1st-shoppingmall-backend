package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.DTO.Product.ProductCategoryRequest
import com.devbabys.shoppingmall.DTO.Product.ProductCategoryResponse
import com.devbabys.shoppingmall.Security.JwtUtil
import com.devbabys.shoppingmall.Service.ImageService
import com.devbabys.shoppingmall.Service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@RestController
class ProductController(
    @Autowired private val productService: ProductService,
    @Autowired private val imageService: ImageService,
    @Autowired private val jwtUtil: JwtUtil
) {
    @GetMapping("product/category/list")
    fun getProduct(): ResponseEntity<Map<String, Any>> {
        val (response, description, value) = productService.getCategoryList()
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    @PostMapping("product/category/add")
    fun addCategory(@RequestBody productCategoryRequest: ProductCategoryRequest): ResponseEntity<Map<String, String>> {
        val (response, description, value) = productService.addCategory(productCategoryRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    @PutMapping("product/category/update")
    fun updateCategory(@RequestBody productCategoryRequest: ProductCategoryRequest): ResponseEntity<Map<String, String>> {
        val (response, description, value) = productService.updateCategory(productCategoryRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    @PostMapping("product/category/delete")
    fun deleteCategory(@RequestBody productCategoryResponse: ProductCategoryResponse): ResponseEntity<Map<String, String>> {
        val (response, description, value) = productService.deleteCategory(productCategoryResponse)
        val result = mapOf("result" to response, "description" to description, "value" to value)
        
        return ResponseEntity.ok(result)
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