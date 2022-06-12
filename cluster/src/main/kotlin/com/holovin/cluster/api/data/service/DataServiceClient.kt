package com.holovin.cluster.api.data.service

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

@FeignClient("dataServiceClient", url = "localhost:8082")
interface DataServiceClient {

    @PostMapping("/saveLab", consumes = ["multipart/form-data"])
    fun saveLab(
        @RequestPart multipartFile: MultipartFile,
        @RequestParam labFolder: String,
        @RequestParam labName: String
    )

    @PostMapping("/saveLabs", consumes = ["multipart/form-data"])
    fun saveLabs(
        @RequestPart multipartFile: MultipartFile,
        @RequestParam labFolder: String,
    )

    @PostMapping("/getTemplate")
    fun getTemplate(
        @RequestParam labFolder: String,
    ) : ByteArray

    @PostMapping("/getFromGitHub")
    fun getFromGitHub(
        @RequestParam labFolder: String,
        @RequestParam labName: String,
        @RequestParam ownerReposUrl: String
    )
}
