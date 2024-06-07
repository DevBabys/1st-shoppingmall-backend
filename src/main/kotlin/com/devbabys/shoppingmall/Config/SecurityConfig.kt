package com.devbabys.shoppingmall.Config

import com.devbabys.shoppingmall.Security.JwtRequestFilter
import com.devbabys.shoppingmall.Service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.servlet.config.annotation.CorsRegistry

@Configuration
class SecurityConfig(
    private val customUserDetailsService: CustomUserDetailsService,
    private val jwtRequestFilter: JwtRequestFilter
) {
    @Bean
    @Throws(Exception::class)
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        println("########## Config : SecurityConfig : authenticationManager ##########")
        return authenticationConfiguration.getAuthenticationManager()
        //return authenticationConfiguration.authenticationManager as ProviderManager
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder(10)

    private val allowedUrls = arrayOf("/",
        "/css/**", "/js/**", "/images/**", // 정적 자원에 대한 접근 허용
        "/user/register", "/user/login", "user/logout", // 로그인 관련
        "user/requestcode", // 인증 관련
        "/user/getuser", "user/updateuser", "user/findemail", "user/finduser", "user/resetpw", "user/deluser", "/user/cart",  // 회원 관련
        "/product/list", // 상품 페이지 관련
        "/product/create"
    )

    @Bean
    protected fun securityFilterChain (http: HttpSecurity) = http
        .csrf { it.disable() }
        .authorizeHttpRequests {
            it.requestMatchers(*allowedUrls).permitAll()	// 허용할 URL 주소
                .anyRequest().authenticated()	// 그 외의 모든 요청은 인증 필요
        }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }	// 세션 미사용
        .build()
}