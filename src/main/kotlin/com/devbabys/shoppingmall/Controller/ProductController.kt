package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.DTO.Authentication.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.Product.ProductCategoryRequest
import com.devbabys.shoppingmall.DTO.Product.ProductCategoryResponse
import com.devbabys.shoppingmall.DTO.Product.ProductRequest
import com.devbabys.shoppingmall.DTO.Product.ProductResponse
import com.devbabys.shoppingmall.Service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class ProductController @Autowired constructor(
    private val productService: ProductService
) {
    @GetMapping("product/category/list")
    fun getProduct(): ResponseEntity<Map<String, Any>> {
        val (response, description, value) = productService.getCategoryList()
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    @PostMapping("product/category/add")
    fun addCategory(@RequestHeader("Authorization") authResponse: AuthenticationResponse,
                    @RequestBody categoryRequest: ProductCategoryRequest)
    : ResponseEntity<Map<String, String>> {
        val (response, description, value) = productService.addCategory(categoryRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    @PutMapping("product/category/update")
    fun updateCategory(@RequestHeader("Authorization") authResponse: AuthenticationResponse,
                       @RequestBody categoryRequest: ProductCategoryRequest)
    : ResponseEntity<Map<String, String>> {
        val (response, description, value) = productService.updateCategory(categoryRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    @PostMapping("product/category/delete")
    fun deleteCategory(@RequestHeader("Authorization") authResponse: AuthenticationResponse,
                       @RequestBody categoryRequest: ProductCategoryResponse)
    : ResponseEntity<Map<String, String>> {
        val (response, description, value) = productService.deleteCategory(categoryRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)
        
        return ResponseEntity.ok(result)
    }

    @PostMapping("product/add")
    fun addProduct(@RequestHeader("Authorization") authResponse: AuthenticationResponse,
                   @RequestPart("product") productRequest: ProductRequest,
                   @RequestPart("image") images: List<MultipartFile>)
    : ResponseEntity<Map<String, String>> {
        val (response, description, value) = productService.addProduct(authResponse, productRequest, images)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    @GetMapping("product/list")
    fun listAllProduct(pageable: Pageable): ResponseEntity<Map<String, Any>> {
        val (response, description, value) = productService.getProductAllList(pageable)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    @GetMapping("product/list/{categoryId}")
    fun listCategoryProduct(@PathVariable categoryId : Long, pageable: Pageable): ResponseEntity<Map<String, Any>> {
        val (response, description, value) = productService.getCategoryProductList(categoryId, pageable)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    @GetMapping("product/{productId}")
    fun getProductDetail(@PathVariable productId : Long) : ResponseEntity<Map<String, Any>> {
        val (response, description, value) = productService.getProductDetail(productId)
        val result = mapOf("result" to response, "description" to description, "value" to value)
        return ResponseEntity.ok(result)
    }

    @PutMapping("product/update")
    fun updateProduct(@RequestHeader("Authorization") authResponse: AuthenticationResponse,
                      @RequestPart("product") productRequest: ProductRequest,
                      @RequestPart("image") images: List<MultipartFile>): ResponseEntity<Map<String, String>> {
        val (response, description, value) = productService.updateProduct(authResponse, productRequest, images)
        val result = mapOf("result" to response, "description" to description, "value" to value)
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("product/delete")
    fun deleteProduct(@RequestHeader("Authorization") authResponse: AuthenticationResponse,
                      @RequestBody productResponse: ProductResponse): ResponseEntity<Map<String, String>> {
        val (response, description, value) = productService.deleteProduct(authResponse, productResponse)
        val result = mapOf("result" to response, "description" to description, "value" to value)
        return ResponseEntity.ok(result)
    }
}