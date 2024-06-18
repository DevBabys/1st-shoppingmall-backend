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

    @PostMapping("/files/uploads")
    fun uploadImage(@RequestParam("image") image: MultipartFile, @RequestParam("filename") filename: String): ResponseEntity<Map<String, String>> {
        val response = imageService.uploadImage(image, filename)

        return if (response) {
            ResponseEntity.ok(mapOf("result" to "success", "description" to "uploadImage", "value" to ""))
        } else {
            ResponseEntity.ok(mapOf("result" to "fail", "description" to "uploadImage", "value" to "upload error"))
        }
    }

    @GetMapping("/files/{filename}")
    fun viewImage(@PathVariable filename: String) : ResponseEntity<Resource> {
        println("############################ test : uploadDir $filename")
        val response = imageService.getFile(filename)

        return response
    }

    @DeleteMapping("/files/{filename}")
    fun deleteImage(@PathVariable filename: String) : ResponseEntity<Map<String, String>> {
        println("############################ test : uploadDir $filename")
        val response = imageService.deleteFile(filename)

        return if (response) {
            ResponseEntity.ok(mapOf("result" to "success", "description" to "deleteImage", "value" to ""))
        } else {
            ResponseEntity.ok(mapOf("result" to "fail", "description" to "deleteImage", "value" to "delete file error"))
        }
    }
}