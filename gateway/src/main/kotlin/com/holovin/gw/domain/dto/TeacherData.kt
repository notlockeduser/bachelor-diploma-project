package com.holovin.gw.domain.dto

import org.bson.types.ObjectId

data class TeacherData(
    val name: String? = null,
    val surname: String? = null,
    val email: String? = null,
    var password: String? = null,
)
