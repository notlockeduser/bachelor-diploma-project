package com.holovin.gw.domain.dto

data class LabData(
    val teacherEmail: String?,
    val subject: String?,
    val labNumber: String?,
    val description: String?,
    val acceptedStudentEmails: MutableSet<String>?
)

data class UpdateAccessByEmail(
    val email: String? = null
)

data class UpdateAccessByGroup(
    val group: String? = null
)

data class GitHubUrl(
    val ownerRepos: String? = null
)

data class StudentEmail(
    val email: String? = null
)
