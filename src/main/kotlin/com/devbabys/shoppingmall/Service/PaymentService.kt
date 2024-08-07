package com.devbabys.shoppingmall.Service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URI
import java.net.URLConnection
import java.nio.charset.StandardCharsets

@Service
class PaymentService @Autowired constructor(
    @Value("\${toss.payment.api.key}") private val tossApiKey: String
) {


    fun tossOrder(orderId: Long, price: Long) {
        val responseBody = StringBuilder();
        try {
            val url = URI("https://pay.toss.im/api/v2/payments").toURL()
            val connection: URLConnection = url.openConnection()
            connection.addRequestProperty("Content-Type", "application/json")
            connection.setDoOutput(true)
            connection.setDoInput(true)

            println("test key : " + tossApiKey)
            val objectMapper = jacksonObjectMapper().apply {
                registerKotlinModule()
            }
            val jsonBody = objectMapper.writeValueAsString(
                mapOf(
                    "orderNo" to orderId,
                    "amount" to price,
                    "amountTaxFree" to 0,
                    "productDesc" to "테스트 결제",
                    "apiKey" to tossApiKey,
                    "autoExecute" to true,
                    "resultCallback" to "http://project1.babychat.xyz/pay/toss/callback",
                    "callbackVersion" to "V2",
                    "retUrl" to "http://project1.babychat.xyz/pay/toss/result",
                    "retCancelUrl" to "http://project1.babychat.xyz/pay/toss/result"
                )
            )

            BufferedOutputStream(connection.getOutputStream()).use { bos ->
                bos.write(jsonBody.toByteArray(StandardCharsets.UTF_8))
                bos.flush()
            }

            BufferedReader(InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)).use { br ->
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    responseBody.append(line)
                }
            }
        } catch (e: Exception) {
            responseBody.append(e)
        }

        println(responseBody.toString())
    }
}