package com.holovin.cluster.test.service.domain

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "compile_and_test_data")
data class CompileAndTestData(
    @Id val _id: ObjectId = ObjectId.get(),
    val labFolder: String,
    val labName: String,
    var compileResult: String,
    var testResult: String
)
