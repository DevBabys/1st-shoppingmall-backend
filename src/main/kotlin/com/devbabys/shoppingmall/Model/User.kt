package com.devbabys.shoppingmall.Model

import jakarta.persistence.*
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