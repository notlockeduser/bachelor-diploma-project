package com.holovin.user.service.mongo

import com.holovin.user.service.domain.mongo.LabData
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface LabDataRepository : MongoRepository<LabData, Any> {

    fun findByTeacherEmailAndSubjectAndLabNumber(
        teacherEmail: String,
        subject: String,
        labNumber: String
    ): Optional<LabData>

    fun findByAcceptedStudentEmailsContainsAndTeacherEmailAndSubjectAndLabNumber(
        studentEmail: String,
        teacherEmail: String,
        subject: String,
        labNumber: String
    ): Optional<LabData>

    fun findAllByTeacherEmail(teacherEmail: String): List<LabData>

    fun findAllByAcceptedStudentEmailsContains(teacherEmail: String): List<LabData>
}
