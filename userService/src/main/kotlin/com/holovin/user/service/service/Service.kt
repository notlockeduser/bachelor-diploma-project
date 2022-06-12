package com.holovin.user.service.service

import com.holovin.user.service.api.dto.LabDataForStudent
import com.holovin.user.service.api.dto.LabDataForTeacher
import com.holovin.user.service.api.dto.LabDescription
import com.holovin.user.service.api.dto.LabsDataForTeacher
import com.holovin.user.service.clients.DataServiceClient
import com.holovin.user.service.clients.PlagiarismServiceClient
import com.holovin.user.service.clients.TestServiceClient
import com.holovin.user.service.domain.mongo.LabData
import com.holovin.user.service.domain.mongo.StudentData
import com.holovin.user.service.domain.mongo.TeacherData
import com.holovin.user.service.mongo.LabDataRepository
import com.holovin.user.service.mongo.StudentDataRepository
import com.holovin.user.service.mongo.TeacherDataRepository
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class Service(
    val plagiarismService: PlagiarismServiceClient,
    val dataService: DataServiceClient,
    val testService: TestServiceClient,
    val labDataRepository: LabDataRepository,
    val studentDataRepository: StudentDataRepository,
    val teacherDataRepository: TeacherDataRepository
) {

    ////////////////////////////// Student

    fun addStudent(studentData: StudentData): StudentData {
        return studentDataRepository.save(studentData)
    }

    fun addLab(
        teacherEmail: String,
        studentEmail: String,
        subject: String,
        labNumber: String,
        multipartFile: MultipartFile
    ) {

        val studentData = getStudentFromDbByEmail(studentEmail)
        val teacherData = getTeacherFromDbByEmail(teacherEmail)

        val labData = labDataRepository.findByAcceptedStudentEmailsContainsAndTeacherEmailAndSubjectAndLabNumber(
            studentEmail,
            teacherEmail,
            subject,
            labNumber
        ).get()

        // data service
        dataService.saveLab(multipartFile, labData.createNameLabFolder(), labData.createNameLab(studentData))
    }

    fun addTemplate(
        teacherEmail: String,
        subject: String,
        labNumber: String,
        multipartFile: MultipartFile
    ) {

        val teacherData = getTeacherFromDbByEmail(teacherEmail)

        val labData = labDataRepository.findByTeacherEmailAndSubjectAndLabNumber(
            teacherEmail,
            subject,
            labNumber
        ).get()

        // data service
        dataService.saveLab(multipartFile, labData.createNameLabFolder() + "_template", "template")
    }

    fun checkPlagByStudent(teacherEmail: String, studentEmail: String, subject: String, labNumber: String) {

        val studentFromDbByEmail = getStudentFromDbByEmail(studentEmail)

        val labData = labDataRepository.findByAcceptedStudentEmailsContainsAndTeacherEmailAndSubjectAndLabNumber(
            studentEmail,
            teacherEmail,
            subject,
            labNumber
        ).get()

        plagiarismService.checkLabByStudent(labData.createNameLabFolder(), labData.createNameLab(studentFromDbByEmail))
    }

    fun compileLabByStudent(teacherEmail: String, studentEmail: String, subject: String, labNumber: String) {

        val studentFromDbByEmail = getStudentFromDbByEmail(studentEmail)

        val labData = labDataRepository.findByAcceptedStudentEmailsContainsAndTeacherEmailAndSubjectAndLabNumber(
            studentEmail,
            teacherEmail,
            subject,
            labNumber
        ).get()

        testService.compileSrc(labData.createNameLabFolder(), labData.createNameLab(studentFromDbByEmail))
    }

    fun testLabByStudent(teacherEmail: String, studentEmail: String, subject: String, labNumber: String) {

        val studentFromDbByEmail = getStudentFromDbByEmail(studentEmail)

        val labData = labDataRepository.findByAcceptedStudentEmailsContainsAndTeacherEmailAndSubjectAndLabNumber(
            studentEmail,
            teacherEmail,
            subject,
            labNumber
        ).get()

        testService.runTests(labData.createNameLabFolder(), labData.createNameLab(studentFromDbByEmail))
    }

    // Teacher
    fun addTeacher(teacherData: TeacherData): TeacherData {
        return teacherDataRepository.save(teacherData)
    }

    fun createLabByTeacher(teacherEmail: String, labDescription: LabDescription) {

        getTeacherFromDbByEmail(teacherEmail)

        labDataRepository.save(
            LabData(
                teacherEmail = teacherEmail,
                subject = labDescription.subject,
                labNumber = labDescription.labNumber,
                description = labDescription.description
            )
        )
    }

    fun getLabsByTeacherEmail(teacherEmail: String): List<LabData> {

        getTeacherFromDbByEmail(teacherEmail)
        return labDataRepository.findAllByTeacherEmail(teacherEmail)
    }

    fun getLabByTeacher(teacherEmail: String, subject: String, labNumber: String): LabData {

        getTeacherFromDbByEmail(teacherEmail)
        return labDataRepository.findByTeacherEmailAndSubjectAndLabNumber(teacherEmail, subject, labNumber).get()
    }

    fun getLabInfoByStudent(
        studentEmail: String,
        teacherEmail: String,
        subject: String,
        labNumber: String
    ): LabDataForStudent {

        val studentFromDbByEmail = getStudentFromDbByEmail(studentEmail)
        getTeacherFromDbByEmail(teacherEmail)

        val labData = labDataRepository.findByAcceptedStudentEmailsContainsAndTeacherEmailAndSubjectAndLabNumber(
            studentEmail,
            teacherEmail,
            subject,
            labNumber
        ).get()

        val plagiarismResult =
            plagiarismService.getResultPlag(
                labData.createNameLabFolder(),
                labData.createNameLab(studentFromDbByEmail)
            )

        val resultCompile =
            testService.getResultCompile(labData.createNameLabFolder(), labData.createNameLab(studentFromDbByEmail))

        val resultTest =
            testService.getResultTest(labData.createNameLabFolder(), labData.createNameLab(studentFromDbByEmail))

        return LabDataForStudent(
            teacherEmail,
            subject,
            labNumber,
            labData.description,
            plagiarismResult,
            resultCompile,
            resultTest
        )
    }

    fun getLabInfoByTeacher(
        teacherEmail: String,
        subject: String,
        labNumber: String
    ): LabsDataForTeacher {

        getTeacherFromDbByEmail(teacherEmail)
        val labsDataForTeacher = mutableListOf<LabDataForTeacher>()

        val labData = labDataRepository.findByTeacherEmailAndSubjectAndLabNumber(
            teacherEmail,
            subject,
            labNumber
        ).get()

        labData.acceptedStudentEmails.forEach { email ->
            val studentFromDbByEmail = getStudentFromDbByEmail(email)

            val plagiarismResult =
                plagiarismService.getResultPlag(
                    labData.createNameLabFolder(),
                    labData.createNameLab(studentFromDbByEmail)
                )

            val resultCompile =
                testService.getResultCompile(labData.createNameLabFolder(), labData.createNameLab(studentFromDbByEmail))

            val resultTest =
                testService.getResultTest(labData.createNameLabFolder(), labData.createNameLab(studentFromDbByEmail))

            labsDataForTeacher.add(
                LabDataForTeacher(
                    studentFromDbByEmail,
                    plagiarismResult,
                    resultCompile,
                    resultTest
                )
            )
        }

        return LabsDataForTeacher(
            teacherEmail,
            subject,
            labNumber,
            labData.description,
            labsDataForTeacher
        )
    }


    fun getLabsByStudentEmail(studentEmail: String): List<LabData> {

        getStudentFromDbByEmail(studentEmail)
        return labDataRepository.findAllByAcceptedStudentEmailsContains(studentEmail)
    }

    fun updateStudentAccessByEmail(teacherEmail: String, studentEmail: String, subject: String, labNumber: String) {

        val teacherData = getTeacherFromDbByEmail(teacherEmail)
        val studentData = getStudentFromDbByEmail(studentEmail)

        val labData = labDataRepository.findByTeacherEmailAndSubjectAndLabNumber(teacherEmail, subject, labNumber).get()
        labData.acceptedStudentEmails.add(studentEmail)

        labDataRepository.save(labData)
    }

    fun uploadLabByGithub(
        teacherEmail: String,
        studentEmail: String,
        subject: String,
        labNumber: String,
        ownerRepos: String
    ) {

        val teacherData = getTeacherFromDbByEmail(teacherEmail)
        val studentData = getStudentFromDbByEmail(studentEmail)

        val labData = labDataRepository.findByTeacherEmailAndSubjectAndLabNumber(teacherEmail, subject, labNumber).get()
        dataService.getFromGitHub(labData.createNameLabFolder(), labData.createNameLab(studentData), ownerRepos)
    }

    fun updateStudentsAccessByGroup(teacherEmail: String, group: String, subject: String, labNumber: String) {

        val teacherData = getTeacherFromDbByEmail(teacherEmail)

        val labData = labDataRepository.findByTeacherEmailAndSubjectAndLabNumber(teacherEmail, subject, labNumber).get()

        val studentsInGroup = studentDataRepository.findAllByGroup(group)
        studentsInGroup.map { labData.acceptedStudentEmails.add(it.email) }

        labDataRepository.save(labData)
    }

    fun checkPlagByTeacher(teacherEmail: String, subject: String, labNumber: String) {

        val labData = labDataRepository.findByTeacherEmailAndSubjectAndLabNumber(
            teacherEmail,
            subject,
            labNumber
        ).get()

        labData.acceptedStudentEmails.forEach {
            val studentFromDbByEmail = getStudentFromDbByEmail(it)
            plagiarismService.checkLabByStudent(
                labData.createNameLabFolder(),
                labData.createNameLab(studentFromDbByEmail)
            )
        }
    }

    fun compileLabByTeacher(teacherEmail: String, subject: String, labNumber: String) {
        val labData = labDataRepository.findByTeacherEmailAndSubjectAndLabNumber(
            teacherEmail,
            subject,
            labNumber
        ).get()

        labData.acceptedStudentEmails.forEach {
            val studentFromDbByEmail = getStudentFromDbByEmail(it)
            testService.compileSrc(labData.createNameLabFolder(), labData.createNameLab(studentFromDbByEmail))
        }
    }

    fun testLabByTeacher(teacherEmail: String, subject: String, labNumber: String) {
        val labData = labDataRepository.findByTeacherEmailAndSubjectAndLabNumber(
            teacherEmail,
            subject,
            labNumber
        ).get()

        labData.acceptedStudentEmails.forEach {
            val studentFromDbByEmail = getStudentFromDbByEmail(it)
            testService.runTests(labData.createNameLabFolder(), labData.createNameLab(studentFromDbByEmail))
        }
    }

    fun downloadTemplate(teacherEmail: String, subject: String, labNumber: String): ByteArray {
        val labData = labDataRepository.findByTeacherEmailAndSubjectAndLabNumber(
            teacherEmail,
            subject,
            labNumber
        ).get()

        return dataService.getTemplate(labData.createNameLabFolder())
    }

    fun downloadPlagReport(
        teacherEmail: String,
        studentEmail: String,
        subject: String,
        labNumber: String
    ): ByteArray {
        val studentFromDbByEmail = getStudentFromDbByEmail(studentEmail)
        val labData = labDataRepository.findByAcceptedStudentEmailsContainsAndTeacherEmailAndSubjectAndLabNumber(
            studentEmail,
            teacherEmail,
            subject,
            labNumber
        ).get()

        return plagiarismService.getResultZipWeb(
            labData.createNameLabFolder(),
            labData.createNameLab(studentFromDbByEmail)
        )
    }

    // utils
    fun existsStudentFromDbByEmail(studentEmail: String): Boolean {
        return studentDataRepository.findByEmail(studentEmail).isPresent
    }

    fun existsTeacherFromDbByEmail(teacherEmail: String): Boolean {
        return teacherDataRepository.findByEmail(teacherEmail).isPresent
    }

    fun getStudentFromDbByEmail(studentEmail: String): StudentData {
        val studentDataOptional = studentDataRepository.findByEmail(studentEmail)
        require(studentDataOptional.isPresent) { "No such STUDENT with email = $studentEmail" }
        return studentDataOptional.get()
    }

    fun getTeacherFromDbByEmail(teacherEmail: String): TeacherData {
        val teacherDataOptional = teacherDataRepository.findByEmail(teacherEmail)
        require(teacherDataOptional.isPresent) { "No such TEACHER with email = $teacherEmail" }
        return teacherDataOptional.get()
    }
}
