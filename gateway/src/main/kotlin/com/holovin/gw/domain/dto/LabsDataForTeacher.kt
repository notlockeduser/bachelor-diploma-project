package com.holovin.gw.domain.dto

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

data class DocumentRequest(
    val teacherEmail: String? = null,
    val studentEmail: String? = null,
    val subject: String? = null,
    val labNumber: String? = null,
)