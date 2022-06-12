package com.holovin.test.service.controller

import com.holovin.test.service.api.TestServiceClient
import com.holovin.test.service.service.TestService
import org.springframework.web.bind.annotation.RestController

@RestController
class TestServiceApiController(
    private val testService: TestService
) : TestServiceClient {

    override fun compileSrc(labFolder: String, labName: String) {
        testService.compileSrc(labFolder, labName)
    }

    override fun getResultCompile(labFolder: String, labName: String): String {
        return testService.getResultCompile(labFolder, labName)
    }

    override fun getResultTest(labFolder: String, labName: String): String {
        return testService.getResultTest(labFolder, labName)
    }

    override fun runTests(labFolder: String, labName: String) {
       testService.runTests(labFolder, labName)
    }
}
