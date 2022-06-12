package com.holovin.plagiarism.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class PlagiarismServiceApplication

fun main(args: Array<String>) {
	runApplication<PlagiarismServiceApplication>(*args)
}
