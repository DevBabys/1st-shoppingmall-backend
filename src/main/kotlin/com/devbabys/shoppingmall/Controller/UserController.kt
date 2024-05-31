package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.DTO.AuthenticationRequest
import com.devbabys.shoppingmall.DTO.UserRegisterRequest
import com.devbabys.shoppingmall.Security.JwtUtil
import com.devbabys.shoppingmall.Service.UserService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    @Autowired
    private val userService: UserService,
    @Autowired
    private val jwtUtil: JwtUtil
) {

    // 회원가입
    @PostMapping("user/register")
    fun postRegister(@RequestBody request: UserRegisterRequest): ResponseEntity<Map<String, String>> {
        var (response, description) = userService.register(request.email, request.password, request.username)
        var result = mapOf("result" to response, "description" to description)
        return ResponseEntity.ok(result)
    }

    // 로그인
    @PostMapping("user/login")
    fun postLogin(@RequestBody request: AuthenticationRequest): ResponseEntity<Map<String, String>> {
        //var result = userService.login(request)
        var result = mapOf("result" to "test", "description" to "test")
        return ResponseEntity.ok(result)
//        return if (result) {
//            "redirect:/"
//        } else {
//            "user/login"
//        }
    }

//    // 로그아웃
//    @GetMapping("user/logout")
//    fun getLogout(model: Model,
//                  response: HttpServletResponse
//    ): String {
//        var result = userService.logout(response)
//        model.addAttribute("result", result)
//
//        return if (result) {
//            "redirect:/"
//        } else {
//            "user/login"
//        }
//    }

    @GetMapping("user/test")
    @ResponseBody
    fun test(@RequestHeader("Authorization") token: String?): ResponseEntity<Any> {
    //fun test(@CookieValue("jwt") jwt: String?): String {
        if (token == null) {
            return ResponseEntity.ok("failed")
        } else {
            val jwt = token.substring(7)

            var email = jwtUtil.extractedEmail(jwt)

            if (jwtUtil.validateToken(jwt, email)) {
                return ResponseEntity.ok("success")
            } else {
                return ResponseEntity.ok("token is not valid")
            }
        }
    }


}