package com.holovin.user.service.domain

import com.holovin.user.service.domain.mongo.StudentData

data class LabStudent(
    private val group: StudentData,
    private val surname: String,
    private val name: String
) {
}
