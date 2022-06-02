package com.holovin.gw.client.user.service

import com.holovin.gw.domain.dto.LabData
import com.holovin.gw.domain.dto.LabDescription
import com.holovin.gw.domain.dto.StudentData
import com.holovin.gw.domain.dto.TeacherData
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient("userClient", url = "localhost:8080")
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

    @GetMapping("/findLabsByTeacherEmail")
    fun findLabsByTeacherEmail(@RequestParam teacherEmail: String): List<LabData>
}
