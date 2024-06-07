package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.DTO.AuthenticationRequest
import com.devbabys.shoppingmall.DTO.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.UserRegisterRequest
import com.devbabys.shoppingmall.Model.User
import com.devbabys.shoppingmall.Repository.UserInfoRepo
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
    private val userInfoRepo: UserInfoRepo,
    @Autowired
    private val jwtService: JwtService
) {

    fun register(request: UserRegisterRequest): Triple<String, String, String> {
        try {
            // 이메일 중복 확인
            if (userRepo.findByEmail(request.email) != null) {
                return Triple("fail", "registerUser", "email already exist")
            }
            // 회원가입 기능 수행
            else {
                val hashedPassword = passwordEncoder.encode(request.password)
                val user = User(email = request.email, password = hashedPassword, username = request.username)
                userRepo.save(user)
                return Triple("success", "registerUser", "")
            }
        } catch (e: Exception) {
            println("Controller : UserController : registerUser : [Catch Error] $e")
            return Triple("fail", "registerUser", "program error")
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

    fun logout(authenticationResponse: AuthenticationResponse): Triple<String, String, String> {
        try {
            var response = jwtService.refreshToken(authenticationResponse)
            return if (response) {
                return Triple("success", "logout", "")
            } else {
                return Triple("fail", "logout", "invalid token")
            }
        } catch (e: Exception) {
            println("Controller : UserController : logout : [Catch Error] $e")
            return Triple("fail", "logout", "program error")
        }
    }

    fun userinfo(authenticationResponse: AuthenticationResponse): Triple<String, String, Any> {
        try {
            var response = jwtService.validateToken(authenticationResponse) // return value : email

            var user = userRepo.findByEmail(response)
            var userinfo = userInfoRepo.findByUserId(user)

            if (user != null && userinfo == null) {
                var result = mapOf("email" to user.email, "username" to user.username)
                return Triple("success", "userinfo", result)
            } else if (user != null && userinfo != null) {
                var result = mapOf("email" to user.email, "username" to user.username, "phoneNumber" to userinfo.phoneNumber,
                    "zipcode" to userinfo.zipCode, "address" to userinfo.address, "detailAddress" to userinfo.detailAddress)
                return Triple("success", "userinfo", result)
            } else {
                return Triple("fail", "userinfo", "invalid user")
            }

        } catch (e: Exception) {
            println("Controller : UserController : getUserInfo : [Catch Error] $e")
            var result = mapOf("class" to "program error", "message" to e)

            return Triple("fail", "getUserInfo", "program error")
        }
    }
}