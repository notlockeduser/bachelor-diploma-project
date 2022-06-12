package com.holovin.plagiarism.service.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

interface PlagiarismServiceClient {

    @PostMapping("/checkLabByStudent")
    fun checkLabByStudent(
        @RequestParam labFolder: String,
        @RequestParam studentLabName: String
    ): Float

    @PostMapping("/getResultPlag")
    fun getResultPlag(
        @RequestParam labFolder: String,
        @RequestParam studentLabName: String
    ): String

    @PostMapping("/getResultZipWeb")
    fun getResultZipWeb(
        @RequestParam labFolder: String,
        @RequestParam studentLabName: String
    ): ByteArray
}
