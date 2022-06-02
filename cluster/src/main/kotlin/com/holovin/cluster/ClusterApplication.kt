package com.holovin.cluster

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class ClusterApplication

fun main(args: Array<String>) {
	runApplication<ClusterApplication>(*args)
}
