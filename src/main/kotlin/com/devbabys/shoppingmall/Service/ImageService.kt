package com.devbabys.shoppingmall.Service

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
class ImageService {

    val uploadDir = Paths.get("").toAbsolutePath().toString() + "/uploads"

    fun uploadImage(image: MultipartFile, filename: String): Boolean {
        try {
            val projectDir = Paths.get("").toAbsolutePath().toString()
            val uploadDirPath = "$projectDir/uploads"
            val uploadDir = File(uploadDirPath)
            if (!uploadDir.exists()) {
                uploadDir.mkdirs()
            }

            val file = File(uploadDir, filename)
            FileOutputStream(file).use { fos -> fos.write(image.bytes) }

            return true
        }
        catch (e: IOException) {
            println("########## ImageService : uploadImage : Catch Error : $e")
            return false
        }
    }

    fun getFile(@PathVariable filename: String) : ResponseEntity<Resource> {
        try {
            val filePath: Path = Paths.get(uploadDir).resolve(filename).normalize()
            val resource: Resource = UrlResource(filePath.toUri())
            return if (resource.exists()) {
                ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.filename + "\"")
                    .body(resource)
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: Exception) {
            return ResponseEntity.notFound().build()
        }
    }

    fun deleteFile(@PathVariable filename: String): Boolean {
        try {
            val filePath = Paths.get(uploadDir, filename)
            if (Files.exists(filePath)) {
                Files.delete(filePath)
                return true
            } else {
                throw FileNotFoundException("File not found: $filename")
            }
        } catch (e: Exception) {
            println("########## ImageService : deleteFile : Catch Error : $e")
            return false
        }

    }
}