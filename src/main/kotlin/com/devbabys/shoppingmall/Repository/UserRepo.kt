package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Entity.User
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.lang.Nullable
import org.springframework.stereotype.Repository

@Repository
interface UserRepo: JpaRepository<User, Long> {
    @Nullable
    fun findByEmail(email: String): User?

    @Modifying
    @Transactional
    @Query("UPDATE user u SET u.username = :username, u.password = :password WHERE u.userId = :userId")
    fun updateUsernameAndPasswordById(
        @Param("userId") userId: Long,
        @Param("username") username: String,
        @Param("password") password: String
    ): Int

    @Modifying
    @Transactional
    @Query("UPDATE user u SET u.username = :username WHERE u.userId = :userId")
    fun updateUsernameById(
        @Param("userId") userId: Long,
        @Param("username") username: String
    ): Int
}