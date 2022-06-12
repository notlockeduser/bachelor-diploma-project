package com.holovin.user.service.domain.mongo

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "student_data")
data class StudentData(
    @Id val _id: ObjectId = ObjectId.get(),
    var name: String,
    var surname: String,
    var email: String,
    var group: String,
    var password: String
)
