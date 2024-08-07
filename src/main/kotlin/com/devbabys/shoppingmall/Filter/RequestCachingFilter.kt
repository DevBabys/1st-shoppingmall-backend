package com.devbabys.shoppingmall.Filter

import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

@Component
class RequestCachingFilter : Filter {
    /* #################### 리퀘스트 데이터 캐시화 관련 필터 ####################
    * 요청 데이터를 캐시화 하는 필터
    * 인터셉터에서 요청 데이터의 동적인 데이터가 필요로 하여 Stream 형식으로 값을 확인
    * Stream 값은 한 번만 사용 가능하기 때문에 Request Stream Data를 Cache Data로 변경하여 인터셉터와 컨트롤러 로직에서 수행되도록 함
    * ###########################################################*/
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        println("########## RequestCachingFilter Process ##########")
        if (request is HttpServletRequest) {
            val requestUri = request.requestURI

            // 특정 URL 패턴을 확인하고 필터 처리 여부 결정: 멀티 파트의 경우 캐시화를 하면 오류가 발생하므로 제외함
            if (requestUri.startsWith("/product/add") || requestUri.startsWith("/product/update")) {
                // 특정 URL일 경우 필터 처리를 생략
                chain.doFilter(request, response)
                return
            }

            val wrappedRequest = if (request is HttpServletRequest) CachedBodyHttpServletRequest(request) else request
            chain.doFilter(wrappedRequest, response)
        }
    }

    class CachedBodyHttpServletRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
        private var cachedBody: ByteArray? = null

        init {
            cachedBody = request.inputStream.readBytes()
        }

        override fun getInputStream(): ServletInputStream {
            return CachedServletInputStream(ByteArrayInputStream(cachedBody))
        }

        override fun getReader(): BufferedReader {
            return BufferedReader(InputStreamReader(inputStream))
        }

        private class CachedServletInputStream(private val cachedBodyInputStream: ByteArrayInputStream) : ServletInputStream() {
            override fun read(): Int = cachedBodyInputStream.read()
            override fun isFinished(): Boolean = cachedBodyInputStream.available() == 0
            override fun isReady(): Boolean = true
            override fun setReadListener(listener: ReadListener) {
                throw UnsupportedOperationException("setReadListener is not implemented")
            }
        }
    }
}