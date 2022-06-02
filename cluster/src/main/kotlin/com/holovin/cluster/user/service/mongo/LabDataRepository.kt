package com.holovin.cluster.user.service.mongo

import com.holovin.cluster.user.service.domain.mongo.LabData
import com.holovin.cluster.user.service.domain.mongo.StudentData
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface LabDataRepository : MongoRepository<LabData, Any> {

    fun findByTeacherEmailAndSubjectAndLabNumber(
        teacherEmail: String,
        subject: String,
        labNumber: String
    ): Optional<LabData>

    fun findAllByTeacherEmail(teacherEmail: String): List<LabData>
}
