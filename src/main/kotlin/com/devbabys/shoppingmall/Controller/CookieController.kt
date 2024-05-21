package com.devbabys.shoppingmall.Controller

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CookieController {

    @GetMapping("cookie/token")
    fun getToken(request: HttpServletRequest, cookieName: String): String? {
        val cookies: Array<Cookie>? = request.cookies
        println("########## CookieController : getCookie : [cookies] $cookies ##########")
        if (cookies != null) {
            for (cookie in cookies) {
                println("########## CookieController : [cookie in cookies] name:${cookie.name}/value:${cookie.value} ##########")
                if (cookie.value == cookieName) {
                    println("########## CookieController : getToken : [cookieName] $cookieName ##########")
                    return cookie.value
                }
            }
        }
        return null
    }

    @PostMapping("cookie/token")
    fun setToken(response: HttpServletResponse, cookieName: String, cookieValue: String): Boolean {
        val cookie = Cookie(cookieName, cookieValue)
        cookie.isHttpOnly = true
        cookie.maxAge = 7 * 24 * 60 * 60 // 1 주일
        response.addCookie(cookie)
        println("########## CookieController : setToken : [cookieName:cookieValue] ${cookie.name} : ${cookie.value} ##########")
        return true
    }
}