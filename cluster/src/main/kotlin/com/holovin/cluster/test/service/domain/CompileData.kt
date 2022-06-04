package com.holovin.cluster.test.service.domain

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "compile_data")
data class CompileData(
    @Id val id: ObjectId = ObjectId.get(),
    val labFolder: String,
    val labName: String,
    var compileResult: Boolean
)
