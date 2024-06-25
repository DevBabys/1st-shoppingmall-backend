package com.devbabys.shoppingmall.Interceptor

import com.devbabys.shoppingmall.Config.UriConfig
import com.devbabys.shoppingmall.Repository.UserRepo
import com.devbabys.shoppingmall.Repository.UserRuleRepo
import com.devbabys.shoppingmall.Security.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthorityInterceptor @Autowired constructor(
    private val jwtUtil: JwtUtil,
    private val userRepo: UserRepo,
    private val ruleRepo: UserRuleRepo,
    uriConfig: UriConfig
): HandlerInterceptor {

    private val adminAllowedUrls = uriConfig.getAdminAllowedUrls()
    private val sellerAllowedUrls = uriConfig.getSellerAllowedUrls()
    private val pathMatcher = AntPathMatcher()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        val token = request.getHeader("Authorization").substring(7)
        val email = jwtUtil.extractedEmail(token)

        val user = userRepo.findByEmail(email)
        val userAuth = user?.let { ruleRepo.findByUserId(it) }
            ?: run { httpResponse(response, "fail","this user has not set permissions"); null }

        println("################################ user permissions check $userAuth")

        if ( (isAllowed(adminAllowedUrls, request.requestURI) && userAuth!!.grade > 2)
            || (isAllowed(sellerAllowedUrls, request.requestURI) && userAuth!!.grade > 3) )
        {
            println("adminAllowedUrls preHandle: ${request.requestURI}")
            httpResponse(response, "fail", "user has not permissions")
            return false
        }

        return true
    }

    fun httpResponse (response: HttpServletResponse, result: String, value: String) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.writer.write("{" +
                "\t\"result\": \"$result\",\n" +
                "\t\"description\": \"AuthorityInterceptor\",\n" +
                "\t\"value\": \"$value\"\n" +
                "}")
    }

    fun isAllowed(allowedUrls: Array<String>, requestURI: String): Boolean {
        return allowedUrls.any { pattern -> pathMatcher.match(pattern, requestURI) }
    }
}