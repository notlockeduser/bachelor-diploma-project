package com.holovin.plagiarism.service.controller

import com.holovin.plagiarism.service.api.PlagiarismServiceClient
import com.holovin.plagiarism.service.service.PlagiarismService
import org.springframework.web.bind.annotation.RestController

@RestController
class PlagiarismServiceApiController(
    private val service: PlagiarismService
) : PlagiarismServiceClient {
    override fun checkLabByStudent(labFolder: String, studentLabName: String): Float {
        return service.checkLabByStudent(labFolder, studentLabName)
    }

    override fun getResultPlag(labFolder: String, studentLabName: String): String {
        return service.getResultPlag(labFolder, studentLabName)
    }

    override fun getResultZipWeb(labFolder: String, studentLabName: String): ByteArray {
        return service.getResultZipWeb(labFolder, studentLabName)
    }
}
