package com.devbabys.shoppingmall.Model

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter
import java.time.LocalDateTime

@Getter
@Setter
@Entity(name = "user")
@Table(name = "user")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Long = 0,

    @Column(length = 20, nullable = false, unique = true)
    var email: String,

    @Column(length = 300)
    var password: String,

    @Column(length = 20, nullable = false)
    var username: String,

    @Column(nullable = false, updatable = false)
    var createAt: LocalDateTime = LocalDateTime.now()
)