package com.devbabys.shoppingmall.Cookie

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class CookieUtil {
    fun getToken(request: HttpServletRequest, cookieName: String): String? {
        val cookies: Array<Cookie>? = request.cookies
        if (cookies != null) {
            for (cookie in cookies) {
                println("########## CookieController : [cookie in cookies] name:${cookie.name}/value:${cookie.value} ##########")
                if (cookie.name == cookieName) {
                    println("########## CookieController : getToken : [cookieName] $cookieName ##########")
                    return cookie.value
                }
            }
        }
        return null
    }

    fun setToken(response: HttpServletResponse,
                 cookieName: String,
                 cookieValue: String): Boolean {
        val cookie = Cookie(cookieName, cookieValue)
        cookie.isHttpOnly = true
        cookie.path = "/" // 쿠키 적용 URL 범위 지정
        cookie.maxAge = 7 * 24 * 60 * 60 // 유효기간 1주일
        response.addCookie(cookie)
        println("########## CookieController : setToken : [cookieName:cookieValue] ${cookie.name} : ${cookie.value} ##########")
        return true
    }

    fun delToken(response: HttpServletResponse, cookieName:String): Boolean {
        val cookie = Cookie(cookieName, "")
        cookie.path = "/"
        cookie.maxAge = 0 // 쿠키 만료 시간을 0으로 설정하여 삭제
        response.addCookie(cookie)
        return true
    }
}