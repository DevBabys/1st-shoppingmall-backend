package com.devbabys.shoppingmall.Interceptor

import com.devbabys.shoppingmall.Repository.CartRepo
import com.devbabys.shoppingmall.Repository.UserRepo
import com.devbabys.shoppingmall.Repository.UserRuleRepo
import com.devbabys.shoppingmall.Utility.JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.io.IOException

@Component
class CertainUserAccessInterceptor  @Autowired constructor(
    private val jwtUtil: JwtUtil,
    private val userRepo: UserRepo,
    private val ruleRepo: UserRuleRepo,
    private val cartRepo: CartRepo
): HandlerInterceptor {
    /* #################### 데이터 소유자 확인 관련 인터셉터 ####################
    * 데이터의 소유자(글 작성자, 사용자의 카트 내역 등)를 확인하는 인터셉터
    * 관리자와 해당 데이터의 소유자가 맞는지 확인하고 아닐 경우에는 접근을 차단하는 역할을 함
    * 현재 로직에서는 카트 관련 로직에만 적용되어 있음
    * ###########################################################*/
    private val objectMapper = ObjectMapper() // JSON 처리 매퍼

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // 요청 주소 확인
        val requestURI = request.requestURI

        // JWT 토큰으로 사용자 정보와 권한 확인 확인
        val token = request.getHeader("Authorization").substring(7)
        val email = jwtUtil.extractedEmail(token)
        val user = userRepo.findByEmail(email)
        val userAuth = user?.let { ruleRepo.findByUserId(it) }
            ?: run {
                httpResponse(response, "fail","url: $requestURI / message : this user has not set permissions")
                return false
            }

        // 요청에 JSON 형식의 데이터가 있으면 로직 수행
        if (request.contentType != null && request.contentType.contains("application/json")) {
            try {
                // 예외처리 : 요청 주소가 카트일 경우 >>> 상세 경로는 UriConfig 파일에서 certSellerAllowedUrls 변수 확인
                if (requestURI.startsWith("/cart/")) {
                    val requestData = convertJSONtoString(request, "cartId") // 요청 데이터 문자열로 변환
                    val cart = cartRepo.findById(requestData!!.toLong()).orElse(null)

                    if (cart == null) {
                        httpResponse(response, "fail", "url: $requestURI / message : cartId not found")
                        return false
                    }
                    else if (cart.userId.userId != user.userId && userAuth.grade > 2) {
                        httpResponse(response, "fail", "url: $requestURI / message : user has not edit or delete permissions")
                        return false
                    }
                }
            } catch ( e: Exception) {
                println("Interceptor : CertainUserAccessInterceptor : convertJSONtoString : [Catch Error] $e")
                httpResponse(response, "fail", "url: $requestURI / message : convert JSON to string error")
                return false
            }
        }
        return true
    }

    // http 요청에 대한 결과를 반환하는 함수
    fun httpResponse (response: HttpServletResponse, result: String, value: String) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.writer.write("{" +
                "\t\"result\": \"$result\",\n" +
                "\t\"description\": \"CertainUserAccessInterceptor\",\n" +
                "\t\"value\": \"$value\"\n" +
                "}")
    }

    // 요청에서 확인된 JSON 구조의 데이터를 문자열로 변환하는 함수
    fun convertJSONtoString(request: HttpServletRequest, fieldName: String): String? {
        return try {
            val requestBody = request.inputStream.bufferedReader().use { it.readText() }
            val jsonNode = objectMapper.readTree(requestBody)
            val fieldValue = jsonNode.get(fieldName)?.asText()
            println("Extracted JSON field value: $fieldValue")
            fieldValue
        } catch (e: IOException) {
            println("I/O error reading JSON: ${e.message}")
            null
        }
    }
}