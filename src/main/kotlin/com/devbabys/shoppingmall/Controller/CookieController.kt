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
                if (cookie.name == cookieName) {
                    println("########## CookieController : getToken : [cookieName] $cookieName ##########")
                    return cookie.value
                }
            }
        }
        return null
    }

    /**
     * 토큰 저장
     * {@link HttpServletResponse}
     * <pre>
     *  프리 버전이라서.. 어시스트가 덜되는건가.. 아니면
     *  code with me 라서 그런건가..
     * </pre>
     */
    @PostMapping("cookie/token")
    fun setToken(response: HttpServletResponse,
                 cookieName: String,
                 cookieValue: String): Boolean {
        val cookie = Cookie(cookieName, cookieValue)
        // http 프로토콜에서 작동 OK 0_<
        cookie.isHttpOnly = true
        // 쿠키 적용 URL 범위 지정
        cookie.path = "/"
        // 쿠키 살아 있는 범위
        cookie.maxAge = 7 * 24 * 60 * 60 // 1 주일
        response.addCookie(cookie)
        println("########## CookieController : setToken : [cookieName:cookieValue] ${cookie.name} : ${cookie.value} ##########")
        return true
    }
}