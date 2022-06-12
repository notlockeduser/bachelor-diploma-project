package com.holovin.test.service.domain

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface CompileAndTestDataRepository : MongoRepository<CompileAndTestData, Any> {

    fun findByLabFolderAndLabName(labFolder: String, labName: String): Optional<CompileAndTestData>
}
