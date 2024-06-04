package com.devbabys.shoppingmall.Security

import com.devbabys.shoppingmall.Service.CustomUserDetailsService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException


@Component
class JwtRequestFilter(
    private val customUserDetailsService: CustomUserDetailsService,
    private val jwtUtil: JwtUtil
): OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authorizationHeader = request.getHeader("Authorization")
        var email: String? = null
        var jwt: String? = null

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7)
                println("########## JwtRequestFilter : doFilterInternal : authorizationHeader : [jwt] $jwt ##########")
                email = jwtUtil.extractedEmail(jwt)
            }

            if (email != null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails = customUserDetailsService.loadUserByUsername(email)
                if (jwtUtil.validateToken(jwt!!, userDetails.username)) {
                    val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    )
                    usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
                }
            }
            chain.doFilter(request, response)
        } catch (e: Exception) {
            println("########## JwtRequestFilter : doFilterInternal : [Catch Error] $e ##########")
        }

    }
}