package com.devbabys.shoppingmall.Entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import lombok.Getter
import lombok.Setter

@Getter
@Setter
@Entity
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null,
    @Column(length = 20, unique = true)
    var email : String,
    @Column(length = 300)
    var password : String,
    @Column(length = 20)
    var userName : String
)