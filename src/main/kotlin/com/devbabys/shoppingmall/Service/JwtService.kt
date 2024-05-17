package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.Model.AuthenticationRequest
import com.devbabys.shoppingmall.Model.AuthResponse
import com.devbabys.shoppingmall.Security.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
    private val logger: Logger = LoggerFactory.getLogger(JwtService::class.java)

    fun createAuthenticationToken(authenticationRequest: AuthenticationRequest): AuthResponse {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(authenticationRequest.email, authenticationRequest.password)
            )
        } catch (e: DisabledException) {
            throw RuntimeException("USER_DISABLED", e)
        } catch (e: BadCredentialsException) {
            throw RuntimeException("INVALID_CREDENTIALS", e)
        }
        logger.debug("Authentication request email: {}", authenticationRequest.email)
        println("##### test ${authenticationRequest.email}")

        val userDetails: UserDetails = userDetailsService.loadUserByUsername(authenticationRequest.email)

        logger.debug("Loaded user details email: {}", userDetails.username)
        val jwt: String = jwtUtil.generateToken(userDetails.username)


        return AuthResponse(jwt)
    }

        fun test(): String {
            return "200 OK"
        }
}