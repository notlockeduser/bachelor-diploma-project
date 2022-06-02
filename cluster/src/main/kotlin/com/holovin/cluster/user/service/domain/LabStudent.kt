package com.holovin.cluster.user.service.domain

data class LabStudent(
    private val emailTeacher: String,
    private val subject: String,
    private val labNumber: String,
    private val group: String,
    private val surname: String,
    private val name: String,
) {

    fun createNameLab(): String {
        return emailTeacher + "_" + subject + "_" + labNumber + "_" + group + "_" + surname + "_" + name
    }

    fun createLabFolder(): LabFolder {
        return LabFolder(emailTeacher, subject, labNumber)
    }

    fun createNameLabFolder(): String {
        return LabFolder(emailTeacher, subject, labNumber).createNameFolder()
    }

    companion object
}

