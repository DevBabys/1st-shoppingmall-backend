package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Model.User
import com.devbabys.shoppingmall.Model.UserInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.lang.Nullable
import org.springframework.stereotype.Repository

@Repository
interface UserInfoRepo: JpaRepository<UserInfo, Long> {
    @Nullable
    fun findByUserId(userId: User): UserInfo?
}