package com.holovin.cluster.user.service.api.dto

import com.holovin.cluster.user.service.domain.mongo.StudentData

data class LabsDataForTeacher(
    val teacherEmail: String,
    val subject: String,
    val labNumber: String,
    val description: String,
    val labsData: List<LabDataForTeacher>,
)

data class LabDataForTeacher(
    val studentData: StudentData,
    val plagiarismPercent: String,
    val compileResult: String,
    val testResult: String
)
