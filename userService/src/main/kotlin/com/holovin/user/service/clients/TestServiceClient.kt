package com.holovin.user.service.clients

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient("testServiceClient", url = "localhost:8083")
interface TestServiceClient {

    @PostMapping("/compileSrc")
    fun compileSrc(
        @RequestParam labFolder: String,
        @RequestParam labName: String
    )

    @PostMapping("/getResultCompile")
    fun getResultCompile(
        @RequestParam labFolder: String,
        @RequestParam labName: String
    ): String

    @PostMapping("/getResultTest")
    fun getResultTest(
        @RequestParam labFolder: String,
        @RequestParam labName: String
    ): String

    @PostMapping("/runTests")
    fun runTests(
        @RequestParam labFolder: String,
        @RequestParam labName: String
    )
}
