package com.devbabys.shoppingmall.Filter

import com.devbabys.shoppingmall.Service.CustomUserDetailsService
import com.devbabys.shoppingmall.Utility.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class JwtRequestFilter(
    private val customUserDetailsService: CustomUserDetailsService,
    private val jwtUtil: JwtUtil
): OncePerRequestFilter() {
    /* #################### JWT 인증 관련 필터 ####################
    * 요청 데이터의 헤더에서 Authorization 키의 JWT 토큰 값 확인
    * 토큰 유효성 확인 및 액세스 제한 로직 수행
    * ###########################################################*/
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        println("########## JwtRequestFilter Process ##########")

        val authorizationHeader = request.getHeader("Authorization")
        var email: String? = null
        var jwt: String? = null
        val tokenBlacklist = jwtUtil.getBlacklist() // 로그아웃 시 토큰 폐기를 위해 블랙리스트 사용

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7)

                if (tokenBlacklist.contains(jwt)) {
                    println("########## JwtRequestFilter : doFilterInternal : [Blacklist] $jwt ##########")
                    httpResponse(response, "fail", "token is invalid")
                    return
                } else if (jwtUtil.isTokenExpired(jwt)) {
                    println("########## JwtRequestFilter : doFilterInternal : [Expired] $jwt ##########")
                    httpResponse(response, "fail", "token is expired")
                    return
                } else {
                    println("########## JwtRequestFilter : doFilterInternal : authorizationHeader : [jwt] $jwt ##########")
                    email = jwtUtil.extractedEmail(jwt)
                }
            }

            if (email != null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails = customUserDetailsService.loadUserByUsername(email)
                if (jwtUtil.validateToken(jwt!!, userDetails.username)) {
                    val authentication = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
            chain.doFilter(request, response)

        } catch (e: Exception) {
            println("########## JwtRequestFilter : doFilterInternal : [Catch Error] $e ##########")

            // 토큰 없음
            val error1 = "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted."
            // 데이터베이스에 데이터 없음
            val error2 = "Cannot pass null or empty values to constructor"
            // http 응답 두 번
            val error3 = "Request processing failed: java.lang.IllegalStateException: getWriter() has already been called for this response"
            // 토큰 헤더 정보가 없음
            val error4 = "Request processing failed: java.lang.NullPointerException: getHeader(...) must not be null"

            if (error1 == e.message) {
                httpResponse(response, "fail", "token not exist")
            } else if (error2 == e.message) {
                httpResponse(response, "fail", "not found user in database")
            } else if (error3 == e.message) {
                httpResponse(response, "fail", "http request error")
            } else if (error4 == e.message) {
            httpResponse(response, "fail", "Authorization header value is null")
            } else {
                httpResponse(response, "fail", "program error : ${e.message}")
            }
        }
    }

    fun httpResponse (response: HttpServletResponse, result: String, value: String) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"

        response.writer.write("{" +
                "\t\"result\": \"$result\",\n" +
                "\t\"description\": \"jwtRequestFilter\",\n" +
                "\t\"value\": \"$value\"\n" +
                "}")
    }
}