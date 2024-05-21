package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.DTO.AuthenticationRequest
import com.devbabys.shoppingmall.Security.JwtUtil
import com.devbabys.shoppingmall.Service.CustomUserDetailsService
import com.devbabys.shoppingmall.Service.JwtService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class JwtController (
    @Autowired
    private val jwtUtil: JwtUtil,
    @Autowired
    private val userDetailsService: CustomUserDetailsService,
    @Autowired
    private val jwtService: JwtService
) {

    @PostMapping("user/auth")
    fun postAuth(@RequestBody authenticationRequest: AuthenticationRequest, response: HttpServletResponse): String
     {
        var jwt = jwtService.createAuthenticationToken(authenticationRequest, response)

        return "\"token\" : \"${jwt.token}\""
    }
}
