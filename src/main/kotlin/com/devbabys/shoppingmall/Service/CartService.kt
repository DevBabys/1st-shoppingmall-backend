package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.DTO.Authentication.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.Cart.CartRequest
import com.devbabys.shoppingmall.Entity.Cart
import com.devbabys.shoppingmall.Repository.CartRepo
import com.devbabys.shoppingmall.Repository.ProductImageRepo
import com.devbabys.shoppingmall.Repository.ProductRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CartService @Autowired constructor(
    private val cartRepo: CartRepo,
    private val productRepo: ProductRepo,
    private val imageRepo: ProductImageRepo,
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

            lateinit var cart : Cart
            val isExist = cartRepo.findByProductId(product).orElse(null)
            if (isExist == null) {
                cart = Cart(userId = user, productId = product, quantity = quantity)
            }
            else {
                cart = Cart(cartId = isExist.cartId, userId = user, productId = product, quantity = quantity + isExist.quantity)
            }

            val cartInfo = cartRepo.save(cart)
            return Triple("success", "addCart", cartInfo.cartId.toString())

        } catch (e: Exception){
            return Triple("fail", "addCart", "program error : $e")
        }
    }

    fun getCartList(authResponse: AuthenticationResponse): Triple<String, String, Any> {
        try {
            val user = jwtService.extractedUserInfo(authResponse)
            val cartList = cartRepo.findByUserId(user).orElse(emptyList())
            return if (cartList.isEmpty()) {
                Triple("success", "getCartList", "cart is empty")
            } else {
                var cartResult: List<Any> =  mutableListOf()
                cartList.forEach {
                    val image = imageRepo.findByProductIdAndIsPrimary(it.productId)
                    val temp = mapOf(
                        "cartId" to it.cartId,
                        "userId" to it.userId.userId,
                        "product" to mapOf(
                            "productId" to it.productId.productId,
                            "name" to it.productId.name,
                            "price" to it.productId.price,
                            "quantity" to it.productId.quantity,
                            "image" to image!!.url,
                        ),
                        "quantity" to it.quantity
                    )
                    cartResult = cartResult + temp
                }
                val resultMap = mapOf(
                    "data" to cartResult
                )
                Triple("success", "getCartList", resultMap)
            }
        } catch (e: Exception) {
            return Triple("fail", "getCartList", "program error : $e")
        }
    }

    fun updateCart(authResponse: AuthenticationResponse,
                   cartRequest: CartRequest): Triple<String, String, String> {
        try {
            if (cartRequest.cartId == null || cartRequest.quantity == null) {
                return Triple("fail", "updateCart", "miss match json type data in body")
            } else {
                val cart = cartRepo.findById(cartRequest.cartId).orElse(null)

                if (cartRequest.quantity == 0) {
                    cartRepo.delete(cart)
                } else {
                    cartRepo.updateQuantity(cartRequest.cartId, cartRequest.quantity)
                }

                return Triple("success", "updateCart", cartRequest.cartId.toString())
            }
        } catch (e: Exception) {
            return Triple("fail", "updateCart", "program error : $e")
        }
    }

    fun deleteCart(authResponse: AuthenticationResponse,
                   cartRequest: CartRequest): Triple<String, String, String> {
        try {
            if (cartRequest.cartId == null) {
                return Triple("fail", "deleteCart", "miss match json type data in body")
            } else {
                val cart = cartRepo.findById(cartRequest.cartId).orElse(null)
                cartRepo.delete(cart)
                return Triple("success", "deleteCart", cartRequest.cartId.toString())
            }
        } catch (e: Exception) {
            return Triple("fail", "deleteCart", "program error : $e")
        }
    }
}