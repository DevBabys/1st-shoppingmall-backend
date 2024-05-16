package com.devbabys.shoppingmall.Controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class ProductController {

    @GetMapping("product/{num}")
    @ResponseBody
    fun product(model: Model, @PathVariable num : Int) : String {
        println("num:\t${num}")
        return "ok"
    }
}