package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.DTO.AuthenticationRequest
import com.devbabys.shoppingmall.DTO.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.UserRegisterRequest
import com.devbabys.shoppingmall.Service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    @Autowired
    private val userService: UserService
) {

    // 회원가입
    @PostMapping("user/register")
    fun postRegister(@RequestBody request: UserRegisterRequest): ResponseEntity<Map<String, String>> {
        var (response, description, value) = userService.register(request)
        var result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    // 로그인
    @PostMapping("user/login")
    fun postLogin(@RequestBody request: AuthenticationRequest): ResponseEntity<Map<String, String>> {
        var (response, description, value) = userService.login(request)
        var result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    // 로그아웃
    @GetMapping("user/logout")
    fun getLogout(@RequestHeader("Authorization") request: AuthenticationResponse): ResponseEntity<Map<String, String>> {
        var (response, description, value) = userService.logout(request)
        var result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    @GetMapping("user/userinfo")
    fun getUserInfo(@RequestHeader("Authorization") request: AuthenticationResponse): ResponseEntity<Map<String, Any>> {
        var (response, description, value) = userService.userinfo(request)
        var result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }
}