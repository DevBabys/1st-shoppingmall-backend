package com.devbabys.shoppingmall.Controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HeaderController {
    @GetMapping("user/login")
    fun getLogin(model: Model): String {
        model.addAttribute("title", "login")
        return "user/login"
    }

    @GetMapping("user/sign")
    fun getSign(model: Model): String {
        model.addAttribute("title", "sign")
        return "user/sign"
    }
}