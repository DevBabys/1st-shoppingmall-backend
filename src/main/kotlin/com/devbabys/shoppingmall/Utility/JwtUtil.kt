package com.devbabys.shoppingmall.Utility

import com.devbabys.shoppingmall.DTO.Authentication.AuthenticationResponse
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey
import kotlin.collections.HashMap

@Component
class JwtUtil @Autowired constructor(
    @Value("\${jwt.secret.key}") private val base64SecretKey: String // 테스트용 정적 대칭키
) {
    /* 동적 대칭키
    * 암호화 방식이 동적으로 변경됨.
    * 서버 재시작 시 JWT 알고리즘이 초기화되며 모든 토큰을 사용할 수 없고 재발급해야 함
    * */
    //private val secretKey = Jwts.SIG.HS512.key().build()

    /* 정적 대칭키: 테스트용 */
    // Base64로 인코딩된 고정된 비밀 키 문자열은 최소 256비트의 키를 사용해야 하므로 길게 써야함)
    // SecretKey 객체로 변환
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64SecretKey))

    // 토큰 폐기 리스트
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