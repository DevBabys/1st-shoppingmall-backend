package com.devbabys.shoppingmall.Controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {
    @GetMapping("/")
    fun index() : String {
        return "Welcome to DevBabys!"
    }
}