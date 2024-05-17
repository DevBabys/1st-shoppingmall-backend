package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.Model.AuthResponse
import com.devbabys.shoppingmall.Model.AuthenticationRequest
import com.devbabys.shoppingmall.Security.JwtUtil
import com.devbabys.shoppingmall.Service.CustomUserDetailsService
import com.devbabys.shoppingmall.Service.JwtService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class JwtController (
    @Autowired
    private val authenticationManager: AuthenticationManager,
    @Autowired
    private val jwtUtil: JwtUtil,
    @Autowired
    private val userDetailsService: CustomUserDetailsService,

    @Autowired
    private val jwtService: JwtService
) {

//    @PostMapping("user/auth")
//    fun postAuth(@RequestBody authenticationRequest: AuthenticationRequest)//: AuthResponse
//     {
//        println(authenticationRequest.email)
//        //var token = jwtService.createAuthenticationToken(authenticationRequest)
//        //return //token
//
//
//    }

    private val logger: Logger = LoggerFactory.getLogger(JwtService::class.java)

    @PostMapping("user/auth")
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

    @PostMapping("user/test")
    fun test(): String {
        return "200 OK"
    }
}
