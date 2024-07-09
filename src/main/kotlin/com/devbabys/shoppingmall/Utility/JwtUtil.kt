package com.devbabys.shoppingmall.Utility

import com.devbabys.shoppingmall.DTO.Authentication.AuthenticationResponse
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.HashMap

@Component
class JwtUtil {
    private val secretKey = Jwts.SIG.HS512.key().build()
    val tokenBlacklist = HashMap<String, Long>()

    // JWT 토큰 생성
    fun generateToken(email: String): String {
        val claims: Map<String, Any> = HashMap()
        return Jwts.builder()
            .claims(claims)
            .subject(email)
            .issuedAt(Date())
            .expiration( Date(System.currentTimeMillis() + 60 * 24 * 1000 * 30) ) // 30 Days
            .signWith(secretKey)
            .compact()
    }

    // 토큰을 이용한 사용자의 이메일 주소 추출
    fun extractedEmail(token: String): String {
        return extractAllClaims(token).payload.subject
    }

    // 토큰 만료 검사
    fun isTokenExpired(token: String): Boolean {
        return extractAllClaims(token).payload.expiration.before(Date())
    }

    // 토큰의 정보 추출
    fun extractAllClaims(token: String): Jws<Claims> {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
    }

    // 토큰 유효성 검사
    fun validateToken(token: String, email: String): Boolean {
        val extractedEmail = extractedEmail(token)
        return (extractedEmail == email && !isTokenExpired(token))
    }

    // 토큰 블랙 리스트 추가 (접근 제한)
    fun addBlacklist(authenticationResponse: AuthenticationResponse) {
        val token = authenticationResponse.token.substring(7)
        val expiration = extractAllClaims(token).payload.expiration.time
        tokenBlacklist[token] = expiration
    }

    // 토큰의 블랙 리스트 확인
    fun getBlacklist(): HashMap<String, Long> {
        return tokenBlacklist
    }
}