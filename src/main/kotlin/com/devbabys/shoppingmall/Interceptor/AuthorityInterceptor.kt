package com.devbabys.shoppingmall.Interceptor

import com.devbabys.shoppingmall.Config.UriConfig
import com.devbabys.shoppingmall.Repository.UserRepo
import com.devbabys.shoppingmall.Repository.UserRuleRepo
import com.devbabys.shoppingmall.Utility.JwtUtil
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
    /* #################### 사용자 접근 권한 확인 관련 인터셉터 ####################
    * 권한에 따라 API의 접근 권한을 확인하는 인터셉터
    * 접근 권한은 데이터베이스의 저장되어 있는 권한 테이블의 값을 기초로 함.
    * 현재 로직에서는 관리자 이상의 권한(1,2)과 관리자와 판매자 이상의 권한(1,2,3)으로 설계
    *
    * [데이터베이스 값 정보]
    * 테이블명 : UserRule
    * 컬럼명 : grade
    * 값에 따른 권한 설명
    * --- 1: 최고 관리자
    * --- 2: 관리자
    * --- 3: 판매자
    * --- 4: 구매자
    * ###########################################################*/

    private val adminAllowedUrls = uriConfig.getAdminAllowedUrls() // 관리자만 접근 가능한 경로
    private val sellerAllowedUrls = uriConfig.getSellerAllowedUrls() // 관리자와 판매자만 접근 가능한 경로
    private val pathMatcher = AntPathMatcher() // 요청 URL을 접근 가능한 경로인지 확인하는 객체

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // JWT 토큰으로 사용자 정보 확인
        val token = request.getHeader("Authorization").substring(7)
        val email = jwtUtil.extractedEmail(token)
        val user = userRepo.findByEmail(email)

        // 사용자 정보를 통한 권한 확인
        val userAuth = user?.let { ruleRepo.findByUserId(it) }
            ?: run { httpResponse(response, "fail","this user has not set permissions"); null }

        // 권한 확인
        if ( (isAllowed(adminAllowedUrls, request.requestURI) && userAuth!!.grade > 2)
            || (isAllowed(sellerAllowedUrls, request.requestURI) && userAuth!!.grade > 3) )
        {
            println("adminAllowedUrls preHandle: ${request.requestURI}")
            httpResponse(response, "fail", "user has not permissions")
            return false
        }

        return true
    }

    // http 요청에 대한 결과를 반환하는 함수
    fun httpResponse (response: HttpServletResponse, result: String, value: String) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.writer.write("{" +
                "\t\"result\": \"$result\",\n" +
                "\t\"description\": \"AuthorityInterceptor\",\n" +
                "\t\"value\": \"$value\"\n" +
                "}")
    }

    // 요청 주소가 허용된 주소와 일치하는지 확인하는 함수
    fun isAllowed(allowedUrls: Array<String>, requestURI: String): Boolean {
        return allowedUrls.any { pattern -> pathMatcher.match(pattern, requestURI) }
    }
}