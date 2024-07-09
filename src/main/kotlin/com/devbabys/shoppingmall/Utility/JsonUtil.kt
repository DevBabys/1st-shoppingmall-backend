package com.devbabys.shoppingmall.Utility

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component

/* TODO: 추후 필터나 인터셉터에서 사용될지 몰라서 만들어둠 */
@Component
class JsonUtil {
    val objectMapper = jacksonObjectMapper()

    // 문자열을 Json으로 변환
    fun stringToJson(value: String): JsonNode {
        return objectMapper.readTree(value)
    }

    // Json 데이터에서 키 값에 해당하는 밸류 반환
    fun getValue(value: JsonNode, key: String): String {
        return value.get(key).asText()
    }
}