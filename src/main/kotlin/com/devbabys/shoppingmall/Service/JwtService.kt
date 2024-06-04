package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.DTO.AuthenticationRequest
import com.devbabys.shoppingmall.DTO.AuthenticationResponse
import com.devbabys.shoppingmall.Security.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class JwtService(
    @Autowired
    private val authenticationManager: AuthenticationManager,
    @Autowired
    private val jwtUtil: JwtUtil,
    @Autowired
    private val userDetailsService: CustomUserDetailsService
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

    fun refreshToken(authenticationResponse: AuthenticationResponse) {
        try {
            var email = jwtUtil.extractedEmail(authenticationResponse.token.substring(7))
            jwtUtil.generateToken(email)
        } catch (e: Exception) {
            throw RuntimeException("EXPIRE_TOKEN_EXCEPTION", e)
        }
    }

    fun validateToken(authenticationResponse: AuthenticationResponse): String {
        try {
            var email = jwtUtil.extractedEmail(authenticationResponse.token.substring(7))
            return email
        } catch (e: Exception) {
            throw RuntimeException("EXPIRE_TOKEN_EXCEPTION", e)
        }
    }
}