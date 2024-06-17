package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.DTO.Product.ProductCategoryRequest
import com.devbabys.shoppingmall.DTO.Product.ProductCategoryResponse
import com.devbabys.shoppingmall.DTO.Product.ProductRequest
import com.devbabys.shoppingmall.Model.Product
import com.devbabys.shoppingmall.Repository.ProductRepo
import com.devbabys.shoppingmall.Security.JwtUtil
import com.devbabys.shoppingmall.Service.ImageService
import com.devbabys.shoppingmall.Service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class ProductController(
    @Autowired private val productService: ProductService,
    @Autowired private val imageService: ImageService,
    @Autowired private val jwtUtil: JwtUtil,
    @Autowired private val productRepo: ProductRepo
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

    @PostMapping("product/add")
    fun addProduct(@RequestPart("product") productRequest: ProductRequest,
                   @RequestPart("image") images: List<MultipartFile>)
    : ResponseEntity<Map<String, String>> {
        val (response, description, value) = productService.addProduct(productRequest, images)
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