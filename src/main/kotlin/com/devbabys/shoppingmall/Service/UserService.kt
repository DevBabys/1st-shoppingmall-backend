package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.Model.User
import com.devbabys.shoppingmall.Repository.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    lateinit var repo: UserRepo

    @Autowired
    val passwordEncoder = BCryptPasswordEncoder()

    fun sign(email: String, password: String, userName: String): Boolean {
        try {
            // 이메일 중복 확인
            if (repo.findByEmail(email) != null) {
                return false
            }
            // 회원가입 기능 수행
            else {
                val hashedPassword = passwordEncoder.encode(password)

                val user = User(email = email, password = hashedPassword, userName = userName)

                repo.save(user)

                return true
            }
        } catch (e: Exception) {
            println("Controller : UserController : sign : [Catch Error] $e")
            return false
        }
    }

    fun login(email: String, password: String): Boolean {
        val user = repo.findByEmail(email)

        if (user != null) {

            var result = passwordEncoder.matches(password, user.password)

            print("login result : $result")
            return result
        }
        else {
            print("user not exists")
            return false
        }
    }
}