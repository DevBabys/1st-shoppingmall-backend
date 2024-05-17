package com.devbabys.shoppingmall.Model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import lombok.Getter
import lombok.Setter

@Getter
@Setter
@Entity
class Product (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null,
    var userId: Long?,
    var title: String,
    var content: String
    )