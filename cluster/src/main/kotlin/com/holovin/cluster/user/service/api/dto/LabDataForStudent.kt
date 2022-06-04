package com.holovin.cluster.user.service.api.dto

data class LabDataForStudent(
    val teacherEmail: String,
    val subject: String,
    val labNumber: String,
    val description: String,
    val compileResult: Boolean
)
