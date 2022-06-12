package com.holovin.user.service.domain

data class LabFolder(
    val emailTeacher: String,
    val subject: String,
    val labNumber: String,
) {

    fun createNameFolder(): String {
        return emailTeacher + "_" + subject + "_" + labNumber
    }

    companion object
}
