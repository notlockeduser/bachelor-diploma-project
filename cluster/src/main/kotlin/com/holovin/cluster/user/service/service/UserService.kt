package com.holovin.cluster.user.service.service

import com.holovin.cluster.data.service.DataService
import com.holovin.cluster.plagiarism.service.PlagiarismService
import com.holovin.cluster.test.service.TestService
import com.holovin.cluster.user.service.domain.LabStudent
import com.holovin.cluster.user.service.api.dto.LabDescription
import com.holovin.cluster.user.service.domain.LabFolder
import com.holovin.cluster.user.service.domain.mongo.LabData
import com.holovin.cluster.user.service.domain.mongo.StudentData
import com.holovin.cluster.user.service.domain.mongo.TeacherData
import com.holovin.cluster.user.service.mongo.LabDataRepository
import com.holovin.cluster.user.service.mongo.StudentDataRepository
import com.holovin.cluster.user.service.mongo.TeacherDataRepository
import net.lingala.zip4j.ZipFile
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

    fun updateStudent(studentData: StudentData): StudentData {
        return studentDataRepository.save(studentData)
    }

//    fun addLab(studentId: String, labData: LabStudent, archiveLab: MultipartFile) {
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
//        dataService.saveLab(archiveLab, labData.createNameLabFolder(), labData.createNameLab())
//    }
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
//    fun checkCompileLabByStudent(studentId: String, labData: LabStudent): Result<String> {
//
//        getStudentFromDb(studentId)
//
//        return try {
//            testService.compileSrc(labData.createNameLabFolder(), labData.createNameLab())
//            Result.success("ok")
//        } catch (ex: Exception) {
//            Result.failure(ex)
//        }
//    }
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
//    fun getListOfAccessFolders(studentId: String): List<LabFolder> {
//        return getStudentFromDb(studentId).acceptedFolders.toList()
//    }
//

    // Teacher
    fun addTeacher(teacherData: TeacherData): TeacherData {
        return teacherDataRepository.save(teacherData)
    }

    fun updateTeacher(teacherData: TeacherData) {
        teacherDataRepository.save(teacherData)
    }

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


//    fun updateStudentAccessByEmail(teacherId: String, studentEmail: String, labFolder: LabFolder) {
//
//        val teacherData = getTeacherFromDb(teacherId)
//        val studentData = getStudentFromDbByEmail(studentEmail)
//
//        // add access (update field acceptedFolders)
//        studentData.acceptedFolders.add(labFolder)
//        studentDataRepository.save(studentData)
//    }
//
//    fun updateStudentsAccessByGroup(teacherId: String, group: String, labFolder: LabFolder) {
//
//        val teacherData = getTeacherFromDb(teacherId)
//
//        // add access (update field acceptedFolders)
//        val studentInGroup = studentDataRepository.findAllByGroup(group)
//        studentInGroup.map { it.acceptedFolders.add(labFolder) }
//        studentDataRepository.saveAll(studentInGroup)
//    }

    fun addLabsByTeacher(teacherId: String, labFolder: LabFolder, archiveLab: ZipFile) {

        val teacherData = getTeacherFromDb(teacherId)

        dataService.saveLabs(archiveLab, labFolder.createNameFolder())
    }
//
//    fun addTemplateByTeacher(teacherId: String, labFolder: LabFolder, archiveLab: ZipFile) {
//        val teacherData = getTeacherFromDb(teacherId)
//
//        dataService.saveLab(archiveLab, labFolder.createNameFolder(), "template")
//    }

    // utils
    private fun getStudentFromDb(studentId: String): StudentData {
        val studentDataOptional = studentDataRepository.findById(studentId)
        require(studentDataOptional.isPresent) { "No such STUDENT with id = $studentId" }
        return studentDataOptional.get()
    }

    private fun getTeacherFromDb(teacherId: String): TeacherData {
        val teacherDataOptional = teacherDataRepository.findById(teacherId)
        require(teacherDataOptional.isPresent) { "No such TEACHER with id = $teacherId" }
        return teacherDataOptional.get()
    }

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
