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

    @GetMapping("user/register")
    fun getSign(model: Model): String {
        model.addAttribute("title", "register")
        return "user/register"
    }

    @GetMapping("user/cart")
    fun getCart(model: Model): String {
        model.addAttribute("title", "cart")
        return "user/cart"
    }
}