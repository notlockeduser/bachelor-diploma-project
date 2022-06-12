package com.holovin.cluster.data.service

import org.springframework.data.mongodb.repository.MongoRepository

interface GitHubTokenRepository : MongoRepository<GitHubToken, Any>