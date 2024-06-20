package com.devbabys.shoppingmall.Config

import com.devbabys.shoppingmall.Interceptor.AuthorityInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig @Autowired constructor(
    private val authorityInterceptor: AuthorityInterceptor,
    uriConfig: UriConfig
) : WebMvcConfigurer {

    private val adminAllowedUrls = uriConfig.getAdminAllowedUrls()
    private val sellerAllowedUrls = uriConfig.getSellerAllowedUrls()

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("https://shopping-front-app.vercel.app") // 도메인 지정
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        // 인터셉터 등록
        registry.addInterceptor(authorityInterceptor)
            .addPathPatterns(*adminAllowedUrls, *sellerAllowedUrls) // 인터셉터 적용 URL 패턴
            .excludePathPatterns("/product/category/list") // 제외 URL 패턴
    }
}