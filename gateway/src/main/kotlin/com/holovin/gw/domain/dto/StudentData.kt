package com.holovin.gw.domain.dto

import org.bson.types.ObjectId

data class StudentData(
    val id: ObjectId = ObjectId.get(),
    var name: String? = null,
    var surname: String? = null,
    var group: String? = null,
    var password: String? = null,
    var email: String? = null,
)
