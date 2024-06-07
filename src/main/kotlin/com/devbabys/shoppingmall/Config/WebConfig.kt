package com.devbabys.shoppingmall.Config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://58.238.170.182:4001")  // 허용할 출처를 지정
            .allowedMethods("GET", "POST", "PUT", "DELETE")
    }
}