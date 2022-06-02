package com.holovin.cluster.user.service.domain.mongo

import com.holovin.cluster.user.service.domain.LabFolder
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "lab_data")
data class LabData(
    @Id val id: ObjectId = ObjectId.get(),
    val teacherEmail: String,
    val subject: String,
    val labNumber: String,
    val description: String,
    val acceptedStudentEmails: MutableSet<String> = mutableSetOf()
) {

//    fun createNameLab(): String {
//        return teacherEmail + "_" + subject + "_" + labNumber + "_" + group + "_" + surname + "_" + name
//    }

    fun createLabFolder(): LabFolder {
        return LabFolder(teacherEmail, subject, labNumber)
    }

    fun createNameLabFolder(): String {
        return LabFolder(teacherEmail, subject, labNumber).createNameFolder()
    }

    companion object
}

