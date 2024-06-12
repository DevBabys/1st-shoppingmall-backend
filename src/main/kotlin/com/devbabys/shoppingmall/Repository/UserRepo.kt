package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.lang.Nullable
import org.springframework.stereotype.Repository

@Repository
interface UserRepo: JpaRepository<User, Long> {
    @Nullable
    fun findByEmail(email: String): User
    @Nullable
    fun findByUsername(username: String): User
}