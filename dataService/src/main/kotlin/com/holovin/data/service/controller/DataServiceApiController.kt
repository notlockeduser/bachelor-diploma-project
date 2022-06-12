package com.holovin.data.service.controller

import com.holovin.data.service.api.DataServiceClient
import com.holovin.data.service.service.Service
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class DataServiceApiController(
    private val service: Service
) : DataServiceClient {
    override fun saveLab(multipartFile: MultipartFile, labFolder: String, labName: String) {
        service.saveLab(multipartFile, labFolder, labName)
    }

    override fun saveLabs(multipartFile: MultipartFile, labFolder: String) {
        service.saveLabs(multipartFile, labFolder)
    }

    override fun getTemplate(labFolder: String): ByteArray {
        return service.getTemplate(labFolder)
    }

    override fun getFromGitHub(labFolder: String, labName: String, ownerReposUrl: String) {
        service.getFromGitHub(labFolder, labName, ownerReposUrl)
    }
}
