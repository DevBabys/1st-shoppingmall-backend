package com.devbabys.shoppingmall.Security

import com.devbabys.shoppingmall.Repository.UserRepo
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.HashMap

@Component
class JwtUtil {
    private val secretKey = Jwts.SIG.HS512.key().build();

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

    fun extractedEmail(token: String): String {
        return extractAllClaims(token).payload.subject
    }

    fun extractedUserId(token: String): Long? {
        lateinit var userRepo: UserRepo
        val email: String = extractAllClaims(token).payload.subject
        return userRepo.findByEmail(email).userId
    }

    fun isTokenExpired(token: String): Boolean {
        return extractAllClaims(token).payload.expiration.before(Date())
    }

    fun extractAllClaims(token: String): Jws<Claims> {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
    }

    fun validateToken(token: String, email: String): Boolean {
        val extractedEmail = extractedEmail(token)
        return (extractedEmail == email && !isTokenExpired(token))
    }
}