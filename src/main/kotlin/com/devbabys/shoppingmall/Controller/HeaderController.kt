package com.devbabys.shoppingmall.Controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HeaderController {
    @GetMapping("member/login")
    fun login(model: Model) : String {
        model.addAttribute("title", "login")
        return "member/login"
    }

    @GetMapping("member/sign")
    fun sign(model: Model) : String {
        model.addAttribute("title", "sign")
        return "member/sign"
    }
}