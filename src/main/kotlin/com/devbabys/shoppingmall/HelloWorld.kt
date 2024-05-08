package com.devbabys.shoppingmall

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorldController {

    @GetMapping("/helloworld")
    fun hello(): String {
        return "Hello, World!"
    }
}