package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.DTO.Authentication.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.Product.ProductCategoryRequest
import com.devbabys.shoppingmall.DTO.Product.ProductCategoryResponse
import com.devbabys.shoppingmall.DTO.Product.ProductRequest
import com.devbabys.shoppingmall.DTO.Product.ProductResponse
import com.devbabys.shoppingmall.Entity.Product
import com.devbabys.shoppingmall.Entity.ProductCategory
import com.devbabys.shoppingmall.Entity.ProductImage
import com.devbabys.shoppingmall.Repository.ProductCategoryRepo
import com.devbabys.shoppingmall.Repository.ProductImageRepo
import com.devbabys.shoppingmall.Repository.ProductRepo
import com.devbabys.shoppingmall.Repository.UserRuleRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ProductService @Autowired constructor(
    private val productRepo: ProductRepo,
    private val categoryRepo: ProductCategoryRepo,
    private val imageRepo: ProductImageRepo,
    private val ruleRepo: UserRuleRepo,
    private val jwtService: JwtService,
    private val imageService: ImageService
) {
    val imgUrl = "https://project1.babychat.xyz/files/"

    fun getCategoryList(): Triple<String, String, Any> {
        val resultMap = mapOf(
            "total" to categoryRepo.count(),
            "data" to categoryRepo.findAll()
        )
        return Triple("success", "getCategoryList", resultMap)
    }

    fun addCategory(productCategoryRequest: ProductCategoryRequest): Triple<String, String, String> {
        try {
            if (productCategoryRequest.name == "") {
                return Triple("fail", "addCategory", "category name is empty")
            } else {
                val categoryData = categoryRepo.findByName(productCategoryRequest.name)
                if (categoryData != null) {
                    println("#####################################category : $categoryData")
                    return Triple("fail", "addCategory", "category already exists")
                } else {
                    val category = ProductCategory(name = productCategoryRequest.name, description = productCategoryRequest.description)
                    categoryRepo.save(category)
                    return Triple("success", "addCategory", "")
                }
            }
        } catch (e: Exception) {
            return Triple("fail", "addCategory", "program error : $e")
        }
    }

    fun updateCategory(productCategoryRequest: ProductCategoryRequest)
    : Triple<String, String, String> {
        try {
            if (productCategoryRequest.name == "") {
                return Triple("fail", "updateCategory", "category name to modify is empty")
            } else {
                val category = categoryRepo.findById(productCategoryRequest.categoryId!!)
                if (category.isEmpty) {
                    return Triple("fail", "updateCategory", "category not exist")
                } else {
                    categoryRepo.save(ProductCategory(
                        categoryId = productCategoryRequest.categoryId,
                        name = productCategoryRequest.name,
                        description = productCategoryRequest.description)
                    )
                    return Triple("success", "updateCategory", "")
                }
            }
        } catch (e: Exception) {
            return Triple("fail", "updateCategory", "program error : $e")
        }
    }

    fun deleteCategory(productCategoryResponse: ProductCategoryResponse)
    : Triple<String, String, String> {
        try {
            val category = categoryRepo.findById(productCategoryResponse.categoryId)
            if (category.isEmpty) {
                return Triple("fail", "deleteCategory", "category not exist")
            } else {
                categoryRepo.deleteById(productCategoryResponse.categoryId)
                return Triple("success", "deleteCategory", "")
            }
        } catch (e: Exception) {
            return Triple("fail", "deleteCategory", "program error : $e")
        }
    }

    fun getProductAllList(pageable: Pageable): Triple<String, String, Any> {
        try {
            var customPage = pageable
            if (pageable.pageNumber > 0) {
                customPage = PageRequest.of(pageable.pageNumber - 1, pageable.pageSize)
            }

            val productList = productRepo.findAll(customPage)

            var result: List<Any> =  mutableListOf()
            productList.forEach {
                val image = imageRepo.findByProductIdAndIsPrimary(it)
                val productDetails = mapOf(
                    "productId" to it.productId,
                    "productName" to it.name,
                    "categoryId" to it.categoryId.categoryId,
                    "categoryName" to it.categoryId.name,
                    "primaryUrl" to image?.url,
                    "price" to it.price,
                    "quantity" to it.quantity
                )
                result = result + productDetails
            }

            val resultMap = mapOf(
                "total" to productRepo.count(),
                "data" to result
            )

            return Triple("success", "listAllProduct", resultMap)
        } catch (e: Exception) {
            return Triple("fail", "listAllProduct", "program error : $e")
        }
    }

    fun getCategoryProductList(categoryId: Long, pageable: Pageable): Triple<String, String, Any> {
        try {
            var customPage = pageable
            if (pageable.pageNumber > 0) {
                customPage = PageRequest.of(pageable.pageNumber - 1, pageable.pageSize)
            }

            val category = categoryRepo.findById(categoryId).orElse(null)
                ?: return Triple("fail", "listCategoryProduct", "category not exist")
            val categoryProductList = productRepo.findByCategoryId(category, customPage)

            var result: List<Any> =  mutableListOf()
            categoryProductList.forEach {
                val image = imageRepo.findByProductIdAndIsPrimary(it)
                val productDetails = mapOf(
                    "productId" to it.productId,
                    "categoryId" to it.categoryId.categoryId,
                    "primaryUrl" to image?.url,
                    "price" to it.price,
                    "quantity" to it.quantity
                )
                result = result + productDetails
            }

            val resultMap = mapOf(
                "total" to productRepo.countByCategoryId(category),
                "data" to result
            )

            return Triple("success", "listCategoryProduct", resultMap)
        } catch (e: Exception) {
            return Triple("fail", "listCategoryProduct", "program error : $e")
        }
    }

    fun getProductDetail(productId: Long): Triple<String, String, Any> {
        try {
            val product = productRepo.findById(productId).orElse(null)
                ?: return Triple("fail", "getProductDetail", "product not exist")

            val image = imageRepo.findByProductId(product)

            var imageResult: List<Any> =  mutableListOf()
            image.forEach {
                val temp = mapOf(
                    "imageId" to it.imageId,
                    "url" to it.url,
                    "isPrimary" to it.isPrimary)
                imageResult = imageResult + temp
            }

            val productDetails = mapOf(
                "productId" to product.productId,
                "name" to product.name,
                "description" to product.description,
                "category" to product.categoryId,
                "price" to product.price,
                "quantity" to product.quantity,
                "image" to imageResult,
                "createdAt" to product.createdAt.toString(),
                "updatedAt" to product.updatedAt.toString()
            )

            return Triple("success", "getProductDetail", productDetails)
        } catch (e: Exception) {
            return Triple("fail", "getProductDetail", "program error : $e")
        }
    }

    fun addProduct(authResponse: AuthenticationResponse,
                   productRequest: ProductRequest,
                   images: List<MultipartFile>?): Triple<String, String, String> {
        val category = categoryRepo.findById(productRequest.categoryId).orElse(null)
            ?: return Triple("fail", "addProduct", "category not exist")

        val user = jwtService.extractedUserInfo(authResponse)

        val product = Product(
            name = productRequest.name,
            userId = user,
            description = productRequest.description,
            price = productRequest.price,
            categoryId = category,
            quantity = productRequest.quantity
        )
        val productInfo = productRepo.save(product)

        try {
            var imageIndex = 0
            var isPrimary = true
            val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd_hhmmss"))

            images?.forEach {
                imageIndex += 1
                val fileName = "${productInfo.productId}_${imageIndex}_${it.originalFilename?.split(".")?.get(0)}_${now}.png"
                imageService.uploadImage(it, fileName)

                if (imageIndex != 1 && isPrimary) {
                    isPrimary = false
                }

                if (it.isEmpty) {
                    imageRepo.save(ProductImage(productId = productInfo, url = imgUrl+"not_image.png", isPrimary = isPrimary))
                } else {
                    imageRepo.save(ProductImage(productId = productInfo, url = imgUrl + fileName, isPrimary = isPrimary))
                }
            }
             return Triple("success", "addProduct", product.productId.toString())
        } catch (e: Exception) {
            return Triple("fail", "addProduct", "file upload error : $e")
        }
    }

    fun updateProduct(authResponse: AuthenticationResponse,
                      productRequest: ProductRequest, images: List<MultipartFile>?): Triple<String, String, String> {
        try {
            val product = productRequest.productId?.let { productRepo.findById(it).orElse(null) }
                ?: return Triple("fail", "updateProduct", "product not exist")
            val category = categoryRepo.findById(productRequest.categoryId).orElse(null)
                ?: return Triple("fail", "updateProduct", "category not exist")

            val user = jwtService.extractedUserInfo(authResponse)
            val rule = ruleRepo.findByUserId(user)
            if (rule!!.grade == 3 && product.userId.userId != user.userId) {
                return Triple("fail", "updateProduct", "user not have access. not owned by the user.")
            }

            val productInfo = productRepo.save(
                Product(
                    productId = product.productId,
                    name = productRequest.name,
                    userId = product.userId,
                    description = productRequest.description,
                    price = productRequest.price,
                    categoryId = category,
                    quantity = productRequest.quantity
                )
            )

            var imageIndex = 0
            var isPrimary = true
            val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd_hhmmss"))

            images?.forEach {
                imageIndex += 1
                val fileName = "${productInfo.productId}_${imageIndex}_${it.originalFilename?.split(".")?.get(0)}_${now}.png"

                if (imageIndex != 1 && isPrimary) {
                    isPrimary = false
                }

                if (!it.isEmpty) {
                    if (imageIndex == 1) {
                        val image = imageRepo.findByProductId(product)
                        image.forEach { imageObject -> imageRepo.delete(imageObject) }
                    }
                    imageService.uploadImage(it, fileName)
                    imageRepo.save(ProductImage(productId = productInfo, url = imgUrl+fileName, isPrimary = isPrimary))
                }
            }
            return Triple("success", "updateProduct", "")
        } catch (e: Exception){
            return Triple("fail", "updateProduct", "program error : $e")
        }
    }

    fun deleteProduct(authResponse: AuthenticationResponse,
                      productResponse: ProductResponse): Triple<String, String, String> {
        try {
            val product = productRepo.findById(productResponse.productId).orElse(null)
            val productImage = imageRepo.findById(product.productId)

            /* to do list : 보류, 상품 리뷰 API 개발 후 마저 개발하기로 함 */

            return Triple("success", "deleteProduct", "")
        } catch (e: Exception){
            return Triple("fail", "deleteProduct", "program error : $e")
        }
    }
}