package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Entity.User
import com.devbabys.shoppingmall.Entity.UserRule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRuleRepo: JpaRepository<UserRule, Long> {
    fun findByUserId(userId: User): UserRule?
}