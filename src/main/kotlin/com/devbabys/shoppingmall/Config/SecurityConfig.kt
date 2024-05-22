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
        "/user/sign", "/user/login", "user/logout",
        "/cookie/getCookie", "/cookie/setCookie",
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