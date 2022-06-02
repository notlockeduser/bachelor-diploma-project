package com.holovin.gw.exceptions

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class FileUploadExceptionAdvice : ResponseEntityExceptionHandler() {
    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxUploadSizeExceededException(e: MaxUploadSizeExceededException?): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(String.format("Upload file too large."))
    }
}
