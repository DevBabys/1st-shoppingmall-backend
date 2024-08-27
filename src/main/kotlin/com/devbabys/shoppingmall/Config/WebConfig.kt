package com.devbabys.shoppingmall.Config

import com.devbabys.shoppingmall.Interceptor.AuthorityInterceptor
import com.devbabys.shoppingmall.Interceptor.CertainUserAccessInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig @Autowired constructor(
    private val authorityInterceptor: AuthorityInterceptor,
    private val certainUserAccessInterceptor: CertainUserAccessInterceptor,
    uriConfig: UriConfig
) : WebMvcConfigurer {

    /* AuthorityInterceptor 관련 */
    private val adminAllowedUrls = uriConfig.getAdminAllowedUrls()
    private val sellerAllowedUrls = uriConfig.getSellerAllowedUrls()
    private val exclusiveUrls = uriConfig.getExclusiveUrls()

    /* CertainUserAccessInterceptor 관련 */
    private val certUserAllowedUrls = uriConfig.getCertUserAllowedUrls()
    private val certUserExclusiveUrls = uriConfig.getCertUserExclusiveUrls()

    // CORS 설정
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("https://shopping-front-app.vercel.app") // 도메인 지정
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
    }

    // 인터셉터 설정
    override fun addInterceptors(registry: InterceptorRegistry) {
        // AuthorityInterceptor 인터셉터 등록
        registry.addInterceptor(authorityInterceptor)
            .addPathPatterns(*adminAllowedUrls, *sellerAllowedUrls) // 인터셉터 적용 URL 패턴
            .excludePathPatterns(*exclusiveUrls) // 제외 URL 패턴

        // CertainUserAccessInterceptor 인터셉터 등록
        registry.addInterceptor(certainUserAccessInterceptor)
            .addPathPatterns(*certUserAllowedUrls)
            .excludePathPatterns(*certUserExclusiveUrls)
    }
}