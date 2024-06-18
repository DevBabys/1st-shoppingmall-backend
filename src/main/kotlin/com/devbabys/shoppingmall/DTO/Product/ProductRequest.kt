package com.devbabys.shoppingmall.DTO.Product

import com.fasterxml.jackson.annotation.JsonProperty

data class ProductRequest(
    @JsonProperty("productId") val productId: Long?,
    @JsonProperty("name") val name: String,
    @JsonProperty("description") val description: String?,
    @JsonProperty("price") val price: Long,
    @JsonProperty("categoryId") val categoryId: Long,
    @JsonProperty("quantity") val quantity: Int
)