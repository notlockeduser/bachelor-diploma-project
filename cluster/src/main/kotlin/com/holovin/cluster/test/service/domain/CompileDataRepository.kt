package com.holovin.cluster.test.service.domain

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface CompileDataRepository : MongoRepository<CompileData, Any> {

    fun findByLabFolderAndLabName(labFolder: String, labName: String): Optional<CompileData>
}
