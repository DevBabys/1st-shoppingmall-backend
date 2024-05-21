package com.devbabys.shoppingmall.Controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class IndexController(
    @Autowired
    private val cookieController: CookieController
) {

    @GetMapping("/")
    fun index(model: Model, request: HttpServletRequest) : String {
        var token = cookieController.getToken(request, "token")
        println("########## IndexController : / : [token] $token ##########")
        return "index"
    }
}