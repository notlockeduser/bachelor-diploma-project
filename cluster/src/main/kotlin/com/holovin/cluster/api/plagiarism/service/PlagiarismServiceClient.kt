package com.holovin.cluster.api.plagiarism.service

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient("plagiarismServiceClient", url = "localhost:8084")
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
