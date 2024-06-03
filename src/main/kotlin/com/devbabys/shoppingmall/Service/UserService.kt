package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.Cookie.CookieUtil
import com.devbabys.shoppingmall.DTO.AuthenticationRequest
import com.devbabys.shoppingmall.Model.User
import com.devbabys.shoppingmall.Repository.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired
    private val passwordEncoder: BCryptPasswordEncoder,
    @Autowired
    private val userRepo: UserRepo,
    @Autowired
    private val jwtService: JwtService,
    @Autowired
    private val cookieUtil: CookieUtil
) {

    fun register(email: String, password: String, username: String): Triple<String, String, String> {
        try {
            // 이메일 중복 확인
            if (userRepo.findByEmail(email) != null) {
                return Triple("fail", "register", "email already exist")
            }
            // 회원가입 기능 수행
            else {
                val hashedPassword = passwordEncoder.encode(password)
                val user = User(email = email, password = hashedPassword, username = username)
                userRepo.save(user)
                return Triple("success", "register", "")
            }
        } catch (e: Exception) {
            println("Controller : UserController : register : [Catch Error] $e")
            return Triple("fail", "register", "program error")
        }
    }

    fun login(authenticationRequest: AuthenticationRequest): Triple<String, String, String> {
        try {
            val user = userRepo.findByEmail(authenticationRequest.email)
            if (user != null) {
                var result = passwordEncoder.matches(authenticationRequest.password, user.password)

                if (result) {
                    var auth = jwtService.createAuthenticationToken(authenticationRequest)
                    return Triple("success", "login", auth.token)
                } else {
                    return Triple("fail", "login", "wrong password")
                }
            }
            else {
                return Triple("fail", "login", "wrong email")
            }
        } catch (e: Exception) {
            println("Controller : UserController : login : [Catch Error] $e")
            return Triple("fail", "login", "program error")
        }
    }

//    fun logout(response: HttpServletResponse): Boolean {
//        cookieUtil.delToken(response, "token")
//        return true
//    }
}