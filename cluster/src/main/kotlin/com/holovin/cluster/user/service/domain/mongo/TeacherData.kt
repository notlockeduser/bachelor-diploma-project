package com.holovin.cluster.user.service.domain.mongo

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "teacher_data")
data class TeacherData(
    @Id val _id: ObjectId = ObjectId.get(),
    val name: String,
    val surname: String,
    val email: String,
    var password: String
)
