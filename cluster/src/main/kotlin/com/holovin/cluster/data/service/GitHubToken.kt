package com.holovin.cluster.data.service

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "github_token")
class GitHubToken(
    val token: String
)