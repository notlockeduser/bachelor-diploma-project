package com.holovin.data.service.mongo

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "github_token")
class GitHubToken(
    val token: String
)