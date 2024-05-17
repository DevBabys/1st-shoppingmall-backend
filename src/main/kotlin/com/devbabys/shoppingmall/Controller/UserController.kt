package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.Service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import java.security.MessageDigest

@Controller
class UserController {
    @Autowired
    lateinit var service: UserService

    // 회원가입
    @PostMapping("user/sign")
    fun postSign(model: Model,
                 @RequestParam(value="email") email: String,
                 @RequestParam(value="password") password: String,
                 @RequestParam(value="userName") userName: String
    ): String {
        var result = service.sign(email, password, userName)

        model.addAttribute(("result"), result) // 템플릿에 회원가입 유무 전달

        return if (result) {
            "redirect:/"
        } else {
            "user/sign"
        }
    }

    // 로그인
    @PostMapping("user/login")
    fun postLogin(model: Model, @RequestParam(value="email") email: String, @RequestParam(value="password") password: String): String {
        println("##### ${email.toString()}")

        var result = service.login(email, password)

        model.addAttribute(("result"), result) // 템플릿에 회원가입 유무 전달

        return "user/login"
    }


}