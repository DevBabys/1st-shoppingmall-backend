package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.DTO.Authentication.AuthenticationRequest
import com.devbabys.shoppingmall.DTO.Authentication.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.User.*
import com.devbabys.shoppingmall.DTO.User.UserRegisterRequest
import com.devbabys.shoppingmall.Entity.User
import com.devbabys.shoppingmall.Entity.UserInfo
import com.devbabys.shoppingmall.Entity.UserRule
import com.devbabys.shoppingmall.Repository.UserRuleRepo
import com.devbabys.shoppingmall.Repository.UserInfoRepo
import com.devbabys.shoppingmall.Repository.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
    val passwordEncoder: BCryptPasswordEncoder,
    val userRepo: UserRepo,
    val userInfoRepo: UserInfoRepo,
    val ruleRepo: UserRuleRepo,
    val jwtService: JwtService
) {

    // 비밀번호 찾기 시, 인증 코드 : 로직 생략되었으므로 비활성화
    // private val passwordRessetCodes = ConcurrentHashMap<String, HashMap<String, String>>()

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
                val userData = userRepo.save(user)

                val rule = UserRule(userId = userData)
                ruleRepo.save(rule)

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
                val result = passwordEncoder.matches(authenticationRequest.password, user.password)

                if (result) {
                    val auth = jwtService.createAuthenticationToken(authenticationRequest)
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
        return try {
            val response = jwtService.expireToken(authenticationResponse)
            if (response) {
                Triple("success", "logout", "")
            } else {
                Triple("fail", "logout", "invalid token")
            }
        } catch (e: Exception) {
            println("Service : UserService : logout : [Catch Error] $e")
            return Triple("fail", "logout", "program error $e")
        }
    }

    fun getUser(authenticationResponse: AuthenticationResponse): Triple<String, String, Any> {
        try {
            val response = jwtService.validateToken(authenticationResponse) // return value : email

            val user = userRepo.findByEmail(response)
            val userinfo = user?.let { userInfoRepo.findByUserId(it) }

            if (user != null && userinfo == null) {
                val result = mapOf("email" to user.email, "username" to user.username)
                return Triple("success", "userinfo", result)
            } else if (user != null && userinfo != null) {
                val result = mapOf("email" to user.email, "username" to user.username, "phoneNumber" to userinfo.phoneNumber,
                    "zipcode" to userinfo.zipCode, "address" to userinfo.address, "detailAddress" to userinfo.detailAddress)
                return Triple("success", "getUser", result)
            } else {
                return Triple("fail", "getUser", "invalid user")
            }

        } catch (e: Exception) {
            println("Service : UserService : getUser : [Catch Error] $e")
            return Triple("fail", "getUser", "program error")
        }
    }

    // 2024-08-26 회원 정보는 주소를 사용하지 않기로 협의
    fun updateUser(authenticationResponse: AuthenticationResponse, userInfoRequest: UserInfoRequest): Triple<String, String, String> {
        try {
            val response = jwtService.validateToken(authenticationResponse) // return value : email

            val user = userRepo.findByEmail(response) ?: return Triple("fail", "updateUser", "invalid user")

            //var userInfo = userInfoRepo.findByUserId(user)

            if (!userInfoRequest.password.isNullOrEmpty()) {
                user.password = passwordEncoder.encode(userInfoRequest.password)
            }

            if ( (userInfoRequest.username == "")
//                || (userInfoRequest.phoneNumber == "")
//                || (userInfoRequest.zipCode == "")
//                || (userInfoRequest.address == "")
//                || (userInfoRequest.detailAddress == "")
                ) {
                return Triple("fail", "updateUser", "invalid request")
            }
            else {
//                if (userInfo == null) {
//                    userInfo = UserInfo(userId = user, phoneNumber = userInfoRequest.phoneNumber, zipCode = userInfoRequest.zipCode, detailAddress = userInfoRequest.detailAddress)
//                } else {
//                    userInfo.phoneNumber = userInfoRequest.phoneNumber
//                    userInfo.zipCode = userInfoRequest.zipCode
//                    userInfo.address = userInfoRequest.address
//                    userInfo.detailAddress = userInfoRequest.detailAddress
//                }

                user.username = userInfoRequest.username

                if (userInfoRequest.password == "") {
                    userRepo.updateUsernameById(user.userId, user.username)
                } else {
                    user.password?.let { userRepo.updateUsernameAndPasswordById(user.userId, user.username, it) }
                }
//                userInfoRepo.save(userInfo)

                return Triple("success", "updateUser", user.userId.toString())
            }
        } catch (e: Exception) {
            println("Service : UserService : updateUser : [Catch Error] $e")
            return Triple("fail", "updateUser", "program error")
        }
    }

    fun findEmail(findEmailRequest: FindEmailRequest): Triple<String, String, String> {
        userRepo.findByEmail(findEmailRequest.email).let {
            if (it!= null) {
                return Triple("success", "findEmail", it.email)
            } else {
                return Triple("fail", "findEmail", "invalid username")
            }
        }
    }

    // 비밀번호 재설정 : 유저를 확인하여 인증코드를 발급하는 함수
//    fun findUser(findUserRequest: FindUserRequest): Triple<String, String, String> {
//        userRepo.findByEmail(findUserRequest.email).let {
//            if (it!= null) {
//                if (it.username == findUserRequest.username) {
//                    val passwordResetCode = HashMap<String, String>()
//                    passwordResetCode["email"] = findUserRequest.email
//                    passwordResetCode["code"] = Random.nextLong(100000, 999999).toString()
//                    passwordResetCode["created_at"] = System.currentTimeMillis().toString()
//                    passwordRessetCodes[passwordResetCode["email"].toString()] = passwordResetCode
//                    return Triple("success", "findUser", passwordResetCode["code"].toString())
//                } else {
//                    return Triple("fail", "findUser", "invalid username")
//                }
//            } else {
//                return Triple("fail", "findUser", "invalid email")
//            }
//        }
//    }

        // 비밀번호 재설정 : 비밀번호 인증번호 확인 로직 추가된 함수
//    fun resetPassword(findUserResponse: FindUserResponse): Triple<String, String, String> {
//        try {
//            val passwordResetCode = passwordRessetCodes[findUserResponse.email]
//            if (passwordResetCode == null) {
//                return Triple("fail", "resetPassword", "incorrect email address")
//            } else {
//                if (passwordResetCode["code"].toString() != findUserResponse.code) {
//                    return Triple("fail", "resetPassword", "incorrect code")
//                } else {
//                    // to do : 테스트용 유효시간 10초, 실제로는 5분간 유효하도록 변경해야 함.
//                    val exprieTime = 10 * 1000 // 5 * 60 * 1000
//                    if (System.currentTimeMillis() - (passwordResetCode["created_at"]!!.toLong()) > exprieTime) {
//                        return Triple("fail", "resetPassword", "expired code")
//                    }
//                    else if (findUserResponse.password == "") {
//                        return Triple("fail", "resetPassword", "required password")
//                    }
//                    else {
//                        userRepo.findByEmail(findUserResponse.email).let {
//                            if (it!= null) {
//                                it.password =  passwordEncoder.encode(findUserResponse.password)
//                                userRepo.save(it)
//                                return Triple("success", "resetPassword", "")
//                            } else {
//                                return Triple("fail", "resetPassword", "invalid email")
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            println("Service : UserService : resetPassword : [Catch Error] $e")
//            return Triple("fail", "resetPassword", "program error")
//        }
//    }

    fun resetPassword(findUserResponse: FindUserResponse): Triple<String, String, String> {
        try {
            // 비밀번호 재설정 임시 코드 로직 생략
            if (findUserResponse.password == "") {
                return Triple("fail", "resetPassword", "required password")
            }
            else {
                val user = userRepo.findByEmail(findUserResponse.email)
                println("################ user @#$@#$@#$@ $user")
                    if (user != null) {
                        if (user.username != findUserResponse.username) {
                            return Triple("fail", "resetPassword", "incorrect username")
                        }
                        user.password = passwordEncoder.encode(findUserResponse.password)
                        userRepo.save(user)
                        return Triple("success", "resetPassword", "")
                    } else {
                        return Triple("fail", "resetPassword", "invalid email")
                    }
            }
        } catch (e: Exception) {
            println("Service : UserService : resetPassword : [Catch Error] $e")
            return Triple("fail", "resetPassword", "program error")
        }
    }

    fun deleteUser(authenticationResponse: AuthenticationResponse): Triple<String, String, String> {
        try {
            val response = jwtService.validateToken(authenticationResponse) // return value : email

            val user = userRepo.findByEmail(response)

            if (response == "") {
                return Triple("fail", "deleteUser", "invalid token")
            }
            else if (user != null) {
                val userInfo = userInfoRepo.findByUserId(user)
                if (userInfo != null) {
                    userInfoRepo.delete(userInfo)
                }
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