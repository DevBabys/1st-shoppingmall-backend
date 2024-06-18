package com.devbabys.shoppingmall.Model

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter

@Getter
@Setter
@Entity
@Table(name = "product_category")
data class ProductCategory (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val categoryId: Long = 0,

    @Column(nullable = false, unique = true)
    val name: String,

    @Column(columnDefinition = "TEXT")
    val description: String? = null
)