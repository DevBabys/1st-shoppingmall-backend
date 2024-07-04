package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.DTO.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.Cart.CartRequest
import com.devbabys.shoppingmall.Entity.Cart
import com.devbabys.shoppingmall.Repository.CartRepo
import com.devbabys.shoppingmall.Repository.ProductRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CartService @Autowired constructor(
    private val cartRepo: CartRepo,
    private val productRepo: ProductRepo,
    private val jwtService: JwtService,
) {
    fun addCart(authResponse: AuthenticationResponse,
                cartRequest: CartRequest): Triple<String, String, String> {
        try {
            val user = jwtService.extractedUserInfo(authResponse)
            val product = productRepo.findById(cartRequest.productId!!).orElse(null)
                ?: return Triple("fail", "addCart", "product not exist")
            val quantity = cartRequest.quantity
                ?: return Triple("fail", "addCart", "not input quantity")

            val cart = Cart(userId = user, productId = product, quantity = quantity)
            val cartInfo = cartRepo.save(cart)

            return Triple("success", "addCart", cartInfo.cartId.toString())
        } catch (e: Exception){
            return Triple("fail", "addCart", "program error : $e")
        }
    }
}