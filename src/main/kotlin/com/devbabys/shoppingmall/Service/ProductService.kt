package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.DTO.Product.ProductCategoryRequest
import com.devbabys.shoppingmall.DTO.Product.ProductCategoryResponse
import com.devbabys.shoppingmall.Model.Product
import com.devbabys.shoppingmall.Model.ProductCategory
import com.devbabys.shoppingmall.Model.User
import com.devbabys.shoppingmall.Repository.ProductCategoryRepo
import com.devbabys.shoppingmall.Repository.ProductRepo
import com.devbabys.shoppingmall.Repository.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductService(
    @Autowired private val productRepo: ProductRepo,
    @Autowired private val categoryRepo: ProductCategoryRepo,
    @Autowired private val userRepo: UserRepo
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
}