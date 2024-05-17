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
//    @Value("\${jwt.secret.key}")
//    private lateinit var secret: String
//    @Value("\${jwt.expiration.date}")
//    private lateinit var expiration: String

    private val secretKey = Jwts.SIG.HS256.key().build();

    fun generateToken(email: String): String {
        val claims: Map<String, Any> = HashMap()
        return Jwts.builder()
            .claims(claims)
            .subject(email)
            .issuedAt(Date())
            .signWith(secretKey)
            .compact()
    }

    fun extractedEmail(token: String): String {
        return extractAllClaims(token).payload.subject
    }

    fun extractedUserId(token: String): Long? {
        lateinit var userRepo: UserRepo
        val email: String = extractAllClaims(token).payload.subject
        return userRepo.findByEmail(email).id
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

//    fun getAfter30Days(time: Int): Date {
//        val now = Date()
//        val calendar = Calendar.getInstance()
//        calendar.time = now
//        calendar.add(Calendar.DAY_OF_MONTH, time) // 현재 시간에서 30일 후
//        val expirationDate = calendar.time
//
//        return expirationDate
//    }
}