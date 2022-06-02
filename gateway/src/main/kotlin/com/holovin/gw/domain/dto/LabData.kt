package com.holovin.gw.domain.dto

import org.bson.types.ObjectId

data class LabData(
    val id: ObjectId?,
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

