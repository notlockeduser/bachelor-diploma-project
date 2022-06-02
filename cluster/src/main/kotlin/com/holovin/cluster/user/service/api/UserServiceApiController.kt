package com.holovin.cluster.user.service.api

import com.holovin.cluster.user.service.api.dto.LabDescription
import com.holovin.cluster.user.service.domain.mongo.LabData
import com.holovin.cluster.user.service.service.UserService
import com.holovin.cluster.user.service.domain.mongo.StudentData
import com.holovin.cluster.user.service.domain.mongo.TeacherData
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserServiceApiController(
    private val userService: UserService
) : UserServiceClient {

    override fun addStudent(@RequestBody studentData: StudentData): StudentData {
        return userService.addStudent(studentData)
    }

    override fun addTeacher(@RequestBody teacherData: TeacherData): TeacherData {
        return userService.addTeacher(teacherData)
    }

    override fun existsStudentFromDbByEmail(studentEmail: String): Boolean {
        return userService.existsStudentFromDbByEmail(studentEmail)
    }

    override fun existsTeacherFromDbByEmail(teacherEmail: String): Boolean {
        return userService.existsTeacherFromDbByEmail(teacherEmail)
    }

    override fun getStudentFromDbByEmail(studentEmail: String): StudentData {
        return userService.getStudentFromDbByEmail(studentEmail)
    }

    override fun getTeacherFromDbByEmail(teacherEmail: String): TeacherData {
        return userService.getTeacherFromDbByEmail(teacherEmail)
    }

    override fun addLabTaskByTeacher(teacherEmail: String, labDescription: LabDescription): ResponseEntity<Unit> {
        userService.createLabByTeacher(teacherEmail, labDescription)
        return ResponseEntity.ok(Unit)
    }

    override fun findLabByTeacher(teacherEmail: String, subject: String, labNumber: String): LabData {
        return userService.getLabByTeacher(teacherEmail, subject, labNumber)
    }

    override fun findLabsByTeacherEmail(teacherEmail: String): List<LabData> {
        return userService.getLabsByTeacherEmail(teacherEmail)
    }
}

