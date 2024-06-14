package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.DTO.Product.ProductCategoryRequest
import com.devbabys.shoppingmall.DTO.Product.ProductCategoryResponse
import com.devbabys.shoppingmall.DTO.Product.ProductRequest
import com.devbabys.shoppingmall.Model.Product
import com.devbabys.shoppingmall.Model.ProductCategory
import com.devbabys.shoppingmall.Model.ProductImage
import com.devbabys.shoppingmall.Repository.ProductCategoryRepo
import com.devbabys.shoppingmall.Repository.ProductImageRepo
import com.devbabys.shoppingmall.Repository.ProductRepo
import com.devbabys.shoppingmall.Repository.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.nio.file.Paths

@Service
class ProductService(
    @Autowired private val productRepo: ProductRepo,
    @Autowired private val categoryRepo: ProductCategoryRepo,
    @Autowired private val imageRepo: ProductImageRepo,
    @Autowired private val userRepo: UserRepo,
    @Autowired private val imageService: ImageService
) {
    fun getCategoryList(): Triple<String, String, Any> {
        return Triple("success", "getCategoryList", categoryRepo.findAll())
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

    fun updateCategory(productCategoryRequest: ProductCategoryRequest): Triple<String, String, String> {
        try {
            if (productCategoryRequest.name == "") {
                return Triple("fail", "updateCategory", "category name to modify is empty")
            } else {
                val category = categoryRepo.findById(productCategoryRequest.categoryId!!)
                if (category.isEmpty) {
                    return Triple("fail", "updateCategory", "category not exists")
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

    fun deleteCategory(productCategoryResponse: ProductCategoryResponse): Triple<String, String, String> {
        try {
            val category = categoryRepo.findById(productCategoryResponse.categoryId)
            if (category.isEmpty) {
                return Triple("fail", "deleteCategory", "category not exists")
            } else {
                categoryRepo.deleteById(productCategoryResponse.categoryId)
                return Triple("success", "deleteCategory", "")
            }
        } catch (e: Exception) {
            return Triple("fail", "deleteCategory", "program error : $e")
        }
    }

    fun getProductList(): Triple<String, String, Any> {
        try {
            val productList = productRepo.findAll()

            var result: List<Pair<String, Any>> =  mutableListOf()
            productList.forEach { product ->
                val image = imageRepo.findByProductId(product)
                val categoryId: Pair<String, Long> = Pair("category_id", product.categoryId.categoryId)

                val productDetails = Pair("product",
                    mapOf(
                        "productId" to product.productId,
                        "categoryId" to product.categoryId.categoryId,
                        "primaryUrl" to image?.url,
                        "price" to product.price,
                        "quantity" to product.quantity
                    )
                )
                result = result + productDetails
            }
//            val categoryList = categoryRepo.findAll()
//            val imageList = imageRepo.findAll()

//            productList.forEach { product ->
//                val category = categoryList.find { it.categoryId == product.categoryId }
//                val image = imageList.find { it.productId == product.productId }
//                product.category = category
//                product.image = image
//            }

            return Triple("success", "listProduct", result)
        } catch (e: Exception) {
            return Triple("fail", "listProduct", "program error : $e")
        }
    }

    fun addProduct(productRequest: ProductRequest, images: List<MultipartFile>?): Triple<String, String, String> {
        val category = categoryRepo.findById(productRequest.categoryId).orElse(null)
            ?: return Triple("fail", "addProduct", "category not exists")

        val product = Product(
            name = productRequest.name,
            description = productRequest.description,
            price = productRequest.price,
            categoryId = category,
            quantity = productRequest.quantity
        )
        val productInfo = productRepo.save(product)

        try {
            var imageIndex = 0
            var isPrimary = true
            val url = "http://58.238.170.182:4001/files/"
            images?.forEach { image ->
                imageIndex += 1
                val fileName = "${productInfo.productId}_${imageIndex}_${image.originalFilename}"
                imageService.uploadImage(image, fileName)

                if (imageIndex != 1 && isPrimary) {
                    isPrimary = false
                }
                imageRepo.save(ProductImage(productId = productInfo, url = url+fileName, isPrimary = isPrimary))
            }
            return Triple("success", "addProduct", product.productId.toString())
        } catch (e: Exception) {
            return Triple("fail", "addProduct", "file upload error : $e")
        }
    }
}