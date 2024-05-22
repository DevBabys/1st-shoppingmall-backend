package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.DTO.AuthenticationRequest
import com.devbabys.shoppingmall.DTO.AuthenticationResponse
import com.devbabys.shoppingmall.Security.JwtUtil
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
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

    fun createAuthenticationToken(authenticationRequest: AuthenticationRequest, response: HttpServletResponse): AuthenticationResponse {
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
        val jwt: String = jwtUtil.generateToken(userDetails.username)

        return AuthenticationResponse(jwt)
    }
}