package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Entity.User
import com.devbabys.shoppingmall.Entity.UserInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.lang.Nullable
import org.springframework.stereotype.Repository

@Repository
interface UserInfoRepo: JpaRepository<UserInfo, Long> {
    @Nullable
    fun findByUserId(userId: User): UserInfo?
}