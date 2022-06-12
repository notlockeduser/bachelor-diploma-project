package com.holovin.cluster.user.service.api

import com.holovin.cluster.user.service.api.dto.LabDataForStudent
import com.holovin.cluster.user.service.api.dto.LabDescription
import com.holovin.cluster.user.service.api.dto.LabsDataForTeacher
import com.holovin.cluster.user.service.domain.mongo.LabData
import com.holovin.cluster.user.service.service.UserService
import com.holovin.cluster.user.service.domain.mongo.StudentData
import com.holovin.cluster.user.service.domain.mongo.TeacherData
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse

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

    override fun findLabByTeacher(teacherEmail: String, subject: String, labNumber: String): LabsDataForTeacher {
        return userService.getLabInfoByTeacher(teacherEmail, subject, labNumber)
    }

    override fun findLabByStudent(
        studentEmail: String,
        teacherEmail: String,
        subject: String,
        labNumber: String
    ): LabDataForStudent {
        return userService.getLabInfoByStudent(studentEmail, teacherEmail, subject, labNumber)
    }

    override fun findLabsByTeacherEmail(teacherEmail: String): List<LabData> {
        return userService.getLabsByTeacherEmail(teacherEmail)
    }

    override fun findLabsByStudentEmail(studentEmail: String): List<LabData> {
        return userService.getLabsByStudentEmail(studentEmail)
    }

    override fun updateStudentAccessByEmail(
        teacherEmail: String,
        studentEmail: String,
        subject: String,
        labNumber: String
    ) {
        userService.updateStudentAccessByEmail(teacherEmail, studentEmail, subject, labNumber)
    }

    override fun uploadLabByGithub(
        teacherEmail: String,
        studentEmail: String,
        subject: String,
        labNumber: String,
        ownerRepos: String
    ) {
        userService.uploadLabByGithub(teacherEmail, studentEmail, subject, labNumber, ownerRepos)
    }

    override fun updateStudentAccessByGroup(
        teacherEmail: String,
        group: String,
        subject: String,
        labNumber: String
    ) {
        userService.updateStudentAccessByEmail(teacherEmail, group, subject, labNumber)
    }

    override fun checkPlagByStudent(teacherEmail: String, studentEmail: String, subject: String, labNumber: String) {
        userService.checkPlagByStudent(teacherEmail, studentEmail, subject, labNumber)
    }

    override fun compileLabByStudent(teacherEmail: String, studentEmail: String, subject: String, labNumber: String) {
        userService.compileLabByStudent(teacherEmail, studentEmail, subject, labNumber)
    }

    override fun testLabByStudent(teacherEmail: String, studentEmail: String, subject: String, labNumber: String) {
        userService.testLabByStudent(teacherEmail, studentEmail, subject, labNumber)
    }

    override fun addLabByStudent(
        teacherEmail: String,
        studentEmail: String,
        subject: String,
        labNumber: String,
        multipartFile: MultipartFile
    ) {
        userService.addLab(teacherEmail, studentEmail, subject, labNumber, multipartFile)
    }

    override fun addTemplateByTeacher(
        teacherEmail: String,
        subject: String,
        labNumber: String,
        multipartFile: MultipartFile
    ) {
        userService.addTemplate(teacherEmail, subject, labNumber, multipartFile)
    }

    override fun checkPlagByTeacher(teacherEmail: String, subject: String, labNumber: String) {
        userService.checkPlagByTeacher(teacherEmail, subject, labNumber)
    }

    override fun compileLabByTeacher(teacherEmail: String, subject: String, labNumber: String) {
        userService.compileLabByTeacher(teacherEmail, subject, labNumber)
    }

    override fun testLabByTeacher(teacherEmail: String, subject: String, labNumber: String) {
        userService.testLabByTeacher(teacherEmail, subject, labNumber)
    }

    override fun downloadTemplate(
        teacherEmail: String,
        subject: String,
        labNumber: String
    ): ByteArray {
        return userService.downloadTemplate(teacherEmail, subject, labNumber)
    }

    override fun downloadPlagReport(
        teacherEmail: String,
        studentEmail: String,
        subject: String,
        labNumber: String
    ): ByteArray {
        return userService.downloadPlagReport(teacherEmail, studentEmail, subject, labNumber)
    }
}
