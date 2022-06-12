package com.holovin.test.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class TestServiceApplication

fun main(args: Array<String>) {
	runApplication<TestServiceApplication>(*args)
}
