package com.holovin.data.service.mongo

import org.springframework.data.mongodb.repository.MongoRepository

interface GitHubTokenRepository : MongoRepository<GitHubToken, Any>