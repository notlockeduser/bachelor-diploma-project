package com.holovin.cluster.user.service.service

import com.holovin.cluster.data.service.DataService
import com.holovin.cluster.plagiarism.service.PlagiarismService
import com.holovin.cluster.test.service.TestService
import com.holovin.cluster.user.service.api.dto.LabDataForStudent
import com.holovin.cluster.user.service.api.dto.LabDescription
import com.holovin.cluster.user.service.domain.mongo.LabData
import com.holovin.cluster.user.service.domain.mongo.StudentData
import com.holovin.cluster.user.service.domain.mongo.TeacherData
import com.holovin.cluster.user.service.mongo.LabDataRepository
import com.holovin.cluster.user.service.mongo.StudentDataRepository
import com.holovin.cluster.user.service.mongo.TeacherDataRepository
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class UserService(
    val plagiarismService: PlagiarismService,
    val dataService: DataService,
    val testService: TestService,
    val labDataRepository: LabDataRepository,
    val studentDataRepository: StudentDataRepository,
    val teacherDataRepository: TeacherDataRepository
) {

    ////////////////////////////// Student

    fun addStudent(studentData: StudentData): StudentData {
        return studentDataRepository.save(studentData)
    }

//    fun updateStudent(studentData: StudentData): StudentData {
//        return studentDataRepository.save(studentData)
//    }

    fun addLab(teacherEmail: String, studentEmail: String, subject: String, labNumber: String, multipartFile: MultipartFile) {

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
//
//    fun downloadTemplateLab(studentId: String, labData: LabStudent): ZipFile {
//
//        val studentData = getStudentFromDb(studentId)
//
//        // Check if student has access to folder
//        val labFolder = labData.createLabFolder()
//        require(studentData.acceptedFolders.any { it == labFolder }) {
//            "you does not have access to folder, accepted folder = ${studentData.acceptedFolders} " +
//                    "require folder = $labFolder"
//        }
//
//        // data service
//        return dataService.getTemplate(labData.createNameLabFolder())
//    }
//
//    fun checkPlagLabByStudent(studentId: String, labData: LabStudent): String {
//
//        getStudentFromDb(studentId)
//
//        // plagiarism service
//        val result = plagiarismService.checkLabByStudent(labData.createNameLabFolder(), labData.createNameLab())
//        return result.toString()
//    }

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
//
//    fun checkTestsLabByStudent(studentId: String, labData: LabStudent): Result<String> {
//
//        getStudentFromDb(studentId)
//
//        return try {
//            testService.runTests(labData.createNameLabFolder(), labData.createNameLab())
//            Result.success("ok")
//        } catch (ex: Exception) {
//            Result.failure(ex)
//        }
//    }
//

    // Teacher
    fun addTeacher(teacherData: TeacherData): TeacherData {
        return teacherDataRepository.save(teacherData)
    }

//    fun updateTeacher(teacherData: TeacherData) {
//        teacherDataRepository.save(teacherData)
//    }

//    fun checkPlagLabByTeacher(teacherId: String, labFolder: LabFolder): String {
//
//        getTeacherFromDb(teacherId)
//
//        // plagiarism service
//        val result = plagiarismService.checkLabByTeacher(labFolder.createNameFolder())
//        return result.toString()
//    }

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

    fun getLabByStudent(
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

        val resultCompile =
            testService.getResultCompile(labData.createNameLabFolder(), labData.createNameLab(studentFromDbByEmail))

        return LabDataForStudent(teacherEmail, subject, labNumber, labData.description, resultCompile)
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

    fun updateStudentsAccessByGroup(teacherEmail: String, group: String, subject: String, labNumber: String) {

        val teacherData = getTeacherFromDbByEmail(teacherEmail)

        val labData = labDataRepository.findByTeacherEmailAndSubjectAndLabNumber(teacherEmail, subject, labNumber).get()

        val studentsInGroup = studentDataRepository.findAllByGroup(group)
        studentsInGroup.map { labData.acceptedStudentEmails.add(it.email) }

        labDataRepository.save(labData)
    }

//    fun addLabsByTeacher(teacherId: String, labFolder: LabFolder, archiveLab: ZipFile) {
//
//        val teacherData = getTeacherFromDb(teacherId)
//
//        dataService.saveLabs(archiveLab, labFolder.createNameFolder())
//    }
//
//    fun addTemplateByTeacher(teacherId: String, labFolder: LabFolder, archiveLab: ZipFile) {
//        val teacherData = getTeacherFromDb(teacherId)
//
//        dataService.saveLab(archiveLab, labFolder.createNameFolder(), "template")
//    }

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
