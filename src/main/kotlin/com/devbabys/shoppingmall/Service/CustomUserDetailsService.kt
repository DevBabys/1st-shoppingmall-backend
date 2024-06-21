package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.Entity.User
import com.devbabys.shoppingmall.Repository.UserRepo
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val repo: UserRepo
): UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user: User? = repo.findByEmail(email)
            // ?: throw UsernameNotFoundException("User not found with username: $email")
        println("########## CustomUserDetailsService : [loadUserByUsername] user email : ${user?.email}##########")
        return org.springframework.security.core.userdetails.User(
            user?.email, user?.password, emptyList()
        )
    }
}