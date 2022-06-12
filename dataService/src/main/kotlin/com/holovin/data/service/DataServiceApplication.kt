package com.holovin.data.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class DataServiceApplication

fun main(args: Array<String>) {
	runApplication<DataServiceApplication>(*args)
}
