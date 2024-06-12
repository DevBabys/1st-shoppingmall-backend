package com.devbabys.shoppingmall.Controller

import com.devbabys.shoppingmall.Service.ImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class ImageController(
    @Autowired private val imageService: ImageService,
) {

    @PostMapping("/uploads")
    fun uploadImage(@RequestParam("image") image: MultipartFile) {
        val response = imageService.uploadImage(image)
        println("################################# test : $response")
    }

    @GetMapping("/files/{filename}")
    fun image(@PathVariable filename: String) : ResponseEntity<Resource> {
        println("############################ test : uploadDir $filename")
        val response = imageService.getFile(filename)

        return response
    }
}