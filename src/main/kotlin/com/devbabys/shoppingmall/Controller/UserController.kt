package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.DTO.AuthenticationRequest
import com.devbabys.shoppingmall.Service.JwtService
import com.devbabys.shoppingmall.Service.UserService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.security.MessageDigest

@Controller
class UserController(
    @Autowired
    private val userService: UserService,
    @Autowired
    private val jwtService: JwtService
) {

    // 회원가입
    @PostMapping("user/sign")
    fun postSign(model: Model,
                 @RequestParam(value="email") email: String,
                 @RequestParam(value="password") password: String,
                 @RequestParam(value="userName") userName: String
    ): String {
        var result = userService.sign(email, password, userName)

        model.addAttribute(("result"), result) // 템플릿에 회원가입 유무 전달

        return if (result) {
            "redirect:/"
        } else {
            "user/sign"
        }
    }

    // 로그인
    @PostMapping("user/login")
    @ResponseBody
    fun postLogin(model: Model, @RequestParam(value="email") email: String, @RequestParam(value="password") password: String, response: HttpServletResponse): String {
//        println("##### ${email.toString()}")
//
//        var result = service.login(email, password)
//
//        model.addAttribute(("result"), result) // 템플릿에 회원가입 유무 전달
//
//        return "user/login"

        var authenticationRequest = AuthenticationRequest(email, password)

        var jwt = jwtService.createAuthenticationToken(authenticationRequest, response)

        //return "\"token\" : \"${jwt.token}\""
        return "redirect:user/test"
    }

    @GetMapping("user/test")
    @ResponseBody
    fun test(@CookieValue("jwt") jwt: String?): String {
        if (jwt == null) {
            return "failed"
        } else {
            return "success"
        }
    }


}