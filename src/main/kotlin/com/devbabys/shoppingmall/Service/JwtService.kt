package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.DTO.AuthenticationRequest
import com.devbabys.shoppingmall.DTO.AuthenticationResponse
import com.devbabys.shoppingmall.Entity.User
import com.devbabys.shoppingmall.Repository.UserRepo
import com.devbabys.shoppingmall.Security.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class JwtService @Autowired constructor(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val userDetailsService: CustomUserDetailsService,
    private val userRepo: UserRepo
) {

    fun createAuthenticationToken(authenticationRequest: AuthenticationRequest): AuthenticationResponse {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(authenticationRequest.email, authenticationRequest.password)
            )
        } catch (e: DisabledException) {
            throw RuntimeException("USER_DISABLED", e)
        } catch (e: BadCredentialsException) {
            throw RuntimeException("INVALID_CREDENTIALS", e)
        }

        val userDetails: UserDetails = userDetailsService.loadUserByUsername(authenticationRequest.email)
        val token: String = jwtUtil.generateToken(userDetails.username)

        return AuthenticationResponse(token)
    }

    fun expireToken(authenticationResponse: AuthenticationResponse): Boolean {
        try {
            val token = authenticationResponse.token.substring(7)
            println("##### expireToken : $token #####")
            val isValidate = !jwtUtil.isTokenExpired(token)
            println("##### isValidate : $isValidate #####")
            if (isValidate) {
                jwtUtil.addBlacklist(authenticationResponse)
                return true
            } else {
                return false
            }
        } catch (e: Exception) {
            throw RuntimeException("EXPIRE_TOKEN_EXCEPTION", e)
        }
    }

    fun validateToken(authenticationResponse: AuthenticationResponse): String {
        try {
            val email = jwtUtil.extractedEmail(authenticationResponse.token.substring(7))
            return email
        } catch (e: Exception) {
            throw RuntimeException("EXPIRE_TOKEN_EXCEPTION", e)
        }
    }

    fun extractedUserInfo(authenticationResponse: AuthenticationResponse): User {
        val response = validateToken(authenticationResponse)
        val user = userRepo.findByEmail(response)
        return user!!
    }
}