package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.Cookie.CookieUtil
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class IndexController(
    @Autowired
    private val cookieUtil: CookieUtil
) {

    @GetMapping("/")
    fun index(model: Model, request: HttpServletRequest) : String {
        var token = cookieUtil.getToken(request, "token");
        model.addAttribute("token", token)
        return "index"
    }
}