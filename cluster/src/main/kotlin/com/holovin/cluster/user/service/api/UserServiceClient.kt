package com.holovin.cluster.user.service.api

import com.holovin.cluster.user.service.api.dto.LabDescription
import com.holovin.cluster.user.service.domain.mongo.LabData
import com.holovin.cluster.user.service.domain.mongo.StudentData
import com.holovin.cluster.user.service.domain.mongo.TeacherData
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

interface UserServiceClient {

    // registration
    @PostMapping("/addStudent")
    fun addStudent(@RequestBody studentData: StudentData): StudentData

    @PostMapping("/addTeacher")
    fun addTeacher(@RequestBody teacherData: TeacherData): TeacherData

    @GetMapping("/existsStudentFromDbByEmail")
    fun existsStudentFromDbByEmail(@RequestParam studentEmail: String): Boolean

    @GetMapping("/existsTeacherFromDbByEmail")
    fun existsTeacherFromDbByEmail(@RequestParam teacherEmail: String): Boolean

    @GetMapping("/getStudentFromDbByEmail")
    fun getStudentFromDbByEmail(@RequestParam studentEmail: String): StudentData

    @GetMapping("/getTeacherFromDbByEmail")
    fun getTeacherFromDbByEmail(@RequestParam teacherEmail: String): TeacherData

    // lab
    @PostMapping("/addLabTaskByTeacher")
    fun addLabTaskByTeacher(
        @RequestParam teacherEmail: String,
        @RequestBody labDescription: LabDescription
    ): ResponseEntity<Unit>

    @GetMapping("/findLabByTeacher")
    fun findLabByTeacher(
        @RequestParam teacherEmail: String,
        @RequestParam subject: String,
        @RequestParam labNumber: String
    ): LabData

    @GetMapping("/findLabByStudent")
    fun findLabByStudent(
        @RequestParam studentEmail: String,
        @RequestParam teacherEmail: String,
        @RequestParam subject: String,
        @RequestParam labNumber: String
    ): LabData

    @GetMapping("/findLabsByTeacherEmail")
    fun findLabsByTeacherEmail(@RequestParam teacherEmail: String): List<LabData>

    @GetMapping("/findLabsByStudentEmail")
    fun findLabsByStudentEmail(@RequestParam studentEmail: String): List<LabData>

    @PostMapping("/updateStudentAccessByEmail")
    fun updateStudentAccessByEmail(
        @RequestParam teacherEmail: String,
        @RequestParam studentEmail: String,
        @RequestParam subject: String,
        @RequestParam labNumber: String
    )

    @PostMapping("/updateStudentAccessByGroup")
    fun updateStudentAccessByGroup(
        @RequestParam teacherEmail: String,
        @RequestParam group: String,
        @RequestParam subject: String,
        @RequestParam labNumber: String
    )
}
