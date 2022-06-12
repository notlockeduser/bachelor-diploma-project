package com.holovin.plagiarism.service.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface PlagResultDataRepository : MongoRepository<PlagResultData, Any> {

    fun findByLabFolderAndLabName(labFolder: String, labName: String): Optional<PlagResultData>
}
