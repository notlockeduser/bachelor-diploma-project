package com.holovin.cluster.user.service.mongo

import com.holovin.cluster.user.service.domain.mongo.TeacherData
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface TeacherDataRepository : MongoRepository<TeacherData, Any> {

    fun findByEmail(email: String): Optional<TeacherData>
}
