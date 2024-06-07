package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.DTO.*
import com.devbabys.shoppingmall.DTO.User.*
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
    fun postRegister(@RequestBody userRequest: UserRegisterRequest): ResponseEntity<Map<String, String>> {
        val (response, description, value) = userService.register(userRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    // 로그인
    @PostMapping("user/login")
    fun postLogin(@RequestBody authRequest: AuthenticationRequest): ResponseEntity<Map<String, String>> {
        val (response, description, value) = userService.login(authRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    // 로그아웃
    @GetMapping("user/logout")
    fun getLogout(@RequestHeader("Authorization") authRequest: AuthenticationResponse): ResponseEntity<Map<String, String>> {
        val (response, description, value) = userService.logout(authRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    // 회원정보 보기
    @GetMapping("user/getuser")
    fun getUserInfo(@RequestHeader("Authorization") authRequest: AuthenticationResponse): ResponseEntity<Map<String, Any>> {
        val (response, description, value) = userService.getUser(authRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    // 회원 정보 수정
    @PutMapping("user/update")
    fun updateUserInfo(@RequestHeader("Authorization") authRequest: AuthenticationResponse, @RequestBody userRequest: UserInfoRequest): ResponseEntity<Map<String, String>> {
        val (response, description, value) = userService.updateUser(authRequest, userRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    // 이메일 찾기
    @PostMapping("user/findemail")
    fun findEmail(@RequestBody findEmailRequest: FindEmailRequest): ResponseEntity<Map<String, String>> {
        val (response, description, value) = userService.findEmail(findEmailRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

//    // 비밀번호 찾기1 - 유저를 확인하여 임시 코드 발급
//    @PostMapping("user/finduser")
//    fun findEmail(@RequestBody findUserRequest: FindUserRequest): ResponseEntity<Map<String, String>> {
//        val (response, description, value) = userService.findUser(findUserRequest)
//        val result = mapOf("result" to response, "description" to description, "value" to value)
//
//        return ResponseEntity.ok(result)
//    }
//
//    // 비밀번호 찾기2 - 임시 코드를 확인하여 비밀번호 재설정
//    @PostMapping("user/resetpw")
//    fun resetPassword(@RequestBody findUserResponse: FindUserResponse): ResponseEntity<Map<String, String>> {
//        val (response, description, value) = userService.resetPassword(findUserResponse)
//        val result = mapOf("result" to response, "description" to description, "value" to value)
//
//        return ResponseEntity.ok(result)
//    }

    // 비밀번호 재설정
    @PostMapping("user/resetpw")
    fun resetPassword(@RequestBody findUserResponse: FindUserResponse): ResponseEntity<Map<String, String>> {
        val (response, description, value) = userService.resetPassword(findUserResponse)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }

    // 회원 탈퇴
    @DeleteMapping("user/delete")
    fun deleteUser(@RequestHeader("Authorization") authRequest: AuthenticationResponse): ResponseEntity<Map<String, String>> {
        val (response, description, value) = userService.deleteUser(authRequest)
        val result = mapOf("result" to response, "description" to description, "value" to value)

        return ResponseEntity.ok(result)
    }
}