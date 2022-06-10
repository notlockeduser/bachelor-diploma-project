package com.holovin.cluster.user.service.api.dto

import com.holovin.cluster.user.service.domain.LabStudent

data class LabDataForStudent(
    val teacherEmail: String,
    val subject: String,
    val labNumber: String,
    val description: String,
    val plagiarismPercent: String,
    val compileResult: String,
    val testResult: String
)
