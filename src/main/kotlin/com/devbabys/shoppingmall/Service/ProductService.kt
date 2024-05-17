package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.Model.Product
import com.devbabys.shoppingmall.Model.User
import com.devbabys.shoppingmall.Repository.ProductRepo
import com.devbabys.shoppingmall.Repository.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductService(
    @Autowired private val productRepo: ProductRepo,
    @Autowired private val userRepo: UserRepo
) {

    fun createProduct(userId: Long?, title: String, content: String): Product {
        var post = Product(userId = userId, title = title, content = content)
        return productRepo.save(post)
    }
}