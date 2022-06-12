package com.holovin.plagiarism.service.mongo

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "plag_data")
data class PlagResultData(
    @Id val _id: ObjectId = ObjectId.get(),
    val labFolder: String,
    val labName: String,
    var result: String
)