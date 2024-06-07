package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.DTO.*
import com.devbabys.shoppingmall.DTO.User.*
import com.devbabys.shoppingmall.Model.User
import com.devbabys.shoppingmall.Model.UserInfo
import com.devbabys.shoppingmall.Repository.UserInfoRepo
import com.devbabys.shoppingmall.Repository.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

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

    // 비밀번호 찾기 시, 인증 코드
    private val passwordRessetCodes = ConcurrentHashMap<String, HashMap<String, String>>()


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
            println("Service : UserService : registerUser : [Catch Error] $e")
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
            println("Service : UserService : login : [Catch Error] $e")
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
            println("Service : UserService : logout : [Catch Error] $e")
            return Triple("fail", "logout", "program error")
        }
    }

    fun getUser(authenticationResponse: AuthenticationResponse): Triple<String, String, Any> {
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
                return Triple("success", "getUser", result)
            } else {
                return Triple("fail", "getUser", "invalid user")
            }

        } catch (e: Exception) {
            println("Service : UserService : getUser : [Catch Error] $e")
            var result = mapOf("class" to "program error", "message" to e)

            return Triple("fail", "getUser", "program error")
        }
    }

    fun updateUser(authenticationResponse: AuthenticationResponse, userInfoRequest: UserInfoRequest): Triple<String, String, String> {
        try {
            var response = jwtService.validateToken(authenticationResponse) // return value : email

            val user = userRepo.findByEmail(response)
            val userInfo = userInfoRepo.findByUserId(user)

            if (userInfoRequest.password != "") {
                user.password = passwordEncoder.encode(userInfoRequest.password)
            }

            if ( (userInfoRequest.username == null || userInfoRequest.username == "")
                || (userInfoRequest.phoneNumber == null || userInfoRequest.phoneNumber == "")
                || (userInfoRequest.zipCode == null || userInfoRequest.zipCode == "")
                || (userInfoRequest.address == null || userInfoRequest.address == "")
                || (userInfoRequest.detailAddress == null || userInfoRequest.detailAddress == "")
                ) {
                return Triple("fail", "updateUser", "invalid request")
            }
            else {
                if (userInfo == null) {
                    userInfoRepo.save(
                        UserInfo(userId = user, phoneNumber = userInfoRequest.phoneNumber, zipCode = userInfoRequest.zipCode,
                            address = userInfoRequest.address, detailAddress = userInfoRequest.detailAddress)
                    )
                }
                else {
                    userInfo.phoneNumber = userInfoRequest.phoneNumber
                    userInfo.zipCode = userInfoRequest.zipCode
                    userInfo.address = userInfoRequest.address
                    userInfo.detailAddress = userInfoRequest.detailAddress
                }

                userRepo.save(user)
                userInfoRepo.save(userInfo)

                return Triple("success", "updateUser", "")
            }
        } catch (e: Exception) {
            println("Service : UserService : updateUser : [Catch Error] $e")
            return Triple("fail", "updateUser", "program error")
        }
    }

    fun findEmail(findEmailRequest: FindEmailRequest): Triple<String, String, String> {
        userRepo.findByUsername(findEmailRequest.username).let {
            if (it!= null) {
                return Triple("success", "findEmail", it.email)
            } else {
                return Triple("fail", "findEmail", "invalid username")
            }
        }
    }

    fun findUser(findUserRequest: FindUserRequest): Triple<String, String, String> {
        userRepo.findByEmail(findUserRequest.email).let {
            if (it!= null) {
                if (it.username == findUserRequest.username) {
                    val passwordResetCode = HashMap<String, String>()
                    passwordResetCode["email"] = findUserRequest.email
                    passwordResetCode["code"] = Random.nextLong(100000, 999999).toString()
                    passwordResetCode["created_at"] = System.currentTimeMillis().toString()
                    passwordRessetCodes[passwordResetCode["email"].toString()] = passwordResetCode
                    return Triple("success", "findUser", passwordResetCode["code"].toString())
                } else {
                    return Triple("fail", "findUser", "invalid username")
                }
            } else {
                return Triple("fail", "findUser", "invalid email")
            }
        }
    }

    fun resetPassword(findUserResponse: FindUserResponse): Triple<String, String, String> {
        try {
            val passwordResetCode = passwordRessetCodes[findUserResponse.email]
            if (passwordResetCode == null) {
                return Triple("fail", "resetPassword", "incorrect email address")
            } else {
                if (passwordResetCode["code"].toString() != findUserResponse.code) {
                    return Triple("fail", "resetPassword", "incorrect code")
                } else {
                    // to do : 테스트용 유효시간 10초, 실제로는 5분간 유효하도록 변경해야 함.
                    val exprieTime = 10 * 1000 // 5 * 60 * 1000
                    if (System.currentTimeMillis() - (passwordResetCode["created_at"]!!.toLong()) > exprieTime) {
                        return Triple("fail", "resetPassword", "expired code")
                    }
                    else if (findUserResponse.password == "") {
                        return Triple("fail", "resetPassword", "required password")
                    }
                    else {
                        userRepo.findByEmail(findUserResponse.email).let {
                            if (it!= null) {
                                it.password =  passwordEncoder.encode(findUserResponse.password)
                                userRepo.save(it)
                                return Triple("success", "resetPassword", "")
                            } else {
                                return Triple("fail", "resetPassword", "invalid email")
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("Service : UserService : resetPassword : [Catch Error] $e")
            return Triple("fail", "resetPassword", "program error")
        }
    }

    fun deleteUser(authenticationResponse: AuthenticationResponse): Triple<String, String, String> {
        try {
            var response = jwtService.validateToken(authenticationResponse) // return value : email

            var user = userRepo.findByEmail(response)

            if (response == null || user == null) {
                return Triple("fail", "deleteUser", "invalid token")
            }
            else if (user!= null) {
                userRepo.delete(user)
                return Triple("success", "deleteUser", "")
            } else {
                return Triple("fail", "deleteUser", "invalid user")
            }
        } catch (e: Exception) {
            println("Service : UserService : deleteUser : [Catch Error] $e")
            return Triple("fail", "deleteUser", "program error")
        }
    }
}