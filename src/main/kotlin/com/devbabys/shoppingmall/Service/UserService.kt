package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.Cookie.CookieUtil
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

    fun register(email: String, password: String, username: String): Pair<String, String> {
        try {
            // 이메일 중복 확인
            if (userRepo.findByEmail(email) != null) {
                return "failed" to "email already exisist"
            }
            // 회원가입 기능 수행
            else {
                val hashedPassword = passwordEncoder.encode(password)
                val user = User(email = email, password = hashedPassword, username = username)
                userRepo.save(user)
                return "ok" to "success"
            }
        } catch (e: Exception) {
            println("Controller : UserController : sign : [Catch Error] $e")
            return "failed" to "error"
        }
    }

//    fun login(authenticationRequest: AuthenticationRequest): Pair<String, String> {
//        val user = userRepo.findByEmail(authenticationRequest.email)
//        if (user != null) {
//            var result = passwordEncoder.matches(authenticationRequest.password, user.password)
//
//            if (result) {
//                var auth = jwtService.createAuthenticationToken(authenticationRequest)
//
//                cookieUtil.setToken(response, "token", auth.token)
//            }
//            print("login result : $result")
//            return result
//        }
//        else {
//            print("user not exists")
//            return false
//        }
//    }

//    fun logout(response: HttpServletResponse): Boolean {
//        cookieUtil.delToken(response, "token")
//        return true
//    }
}