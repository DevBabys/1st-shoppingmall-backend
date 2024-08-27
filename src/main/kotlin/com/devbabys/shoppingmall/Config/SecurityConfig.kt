package com.devbabys.shoppingmall.Config

import com.devbabys.shoppingmall.Filter.JwtRequestFilter
import com.devbabys.shoppingmall.Filter.RequestCachingFilter
import com.devbabys.shoppingmall.Service.CustomUserDetailsService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.SecurityContextHolderFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(
    //private val customUserDetailsService: CustomUserDetailsService,
    private val jwtRequestFilter: JwtRequestFilter,
    private val requestCachingFilter: RequestCachingFilter,
    @Value("\${security.cors.url}") private val corsUrl: String
) {
    @Bean
    @Throws(Exception::class)
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        println("########## Config : SecurityConfig : authenticationManager ##########")
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder(10)

    private val allowedUrls = arrayOf("/",
        "/css/**", "/js/**", "/uploads/**", "/files/**", // 정적 자원에 대한 접근 허용
        "/user/register", "/user/login", "/user/logout", // 로그인 관련
        "/user/getuser", "/user/update", "/user/findemail", "/user/finduser", "/user/resetpw", "/user/delete", "/user/cart",  // 회원 관련
        "/product/category/add", "/product/category/list", "/product/category/delete", "/product/category/update", // 상품 카테고리 관련
        "/product/list", "/product/list/*", "/product/*", "/product/add", "/product/update", "/product/delete",// 상품 페이지 관련
        "/cart/list", "/cart/add", "/cart/update", "/cart/delete", // 카트 관련
        "/order/list", "order/list/*", "/order/add", "/order/complete", "/order/fail", "/order/changeState", // 주문 관련
        "/payment/toss/**", "/payment/toss/order",// 결제 관련
        "/test" // 테스트를 위한 URL 경로
    )

    /* 서블릿 필터 체인 처리
    *  1. JWT 필터 : JWT 인증 관련 처리를 필터에서 진행 후 컨트롤러 로직으로 넘겨줌
    *  2. 캐시 필터 : 데이터 소유자 확인(작성자)을 위해 인터셉터에 넘겨줄 request의 stream 데이터를 캐시화하는 필터
    * */
    @Bean
    protected fun securityFilterChain (http: HttpSecurity) =
        http
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(*allowedUrls).permitAll()    // 허용할 URL 주소
                    .anyRequest().authenticated()    // 그 외의 모든 요청은 인증 필요
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }    // 세션 미사용
            .addFilterBefore(requestCachingFilter, SecurityContextHolderFilter::class.java)  // 요청 데이터 캐시화 필터
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java) // JWT 인증 필터
            .build()!!

    // CORS 처리
    @Bean
    protected fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf(corsUrl, "https://babychat.xyz/")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.setAllowedHeaders(listOf("Content-Type", "Authorization"))
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}