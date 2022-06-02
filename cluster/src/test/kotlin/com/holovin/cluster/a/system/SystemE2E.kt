//package com.holovin.cluster.a.system
//
//import com.holovin.cluster.xrandomizer.random
//import com.holovin.cluster.user.service.service.UserService
//import com.holovin.cluster.user.service.domain.mongo.LabData
//import com.holovin.cluster.user.service.domain.LabFolder
//import com.holovin.cluster.user.service.domain.mongo.StudentData
//import com.holovin.cluster.user.service.domain.mongo.TeacherData
//import net.lingala.zip4j.ZipFile
//import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import java.io.File
//
//@SpringBootTest
//internal class SystemE2E {
//
//    @Autowired
//    lateinit var userService: UserService
//
//    @Test
//    fun `should save lab and give result`() {
//        // GIVEN
//        val teacherData = TeacherData.random()
//        val studentData1 = StudentData.random()
//        val studentData2 = StudentData.random()
//
//        val labFolder = LabData.random().createLabFolder()
//
//        val labData1 = createLabData(labFolder, studentData1)
//        val labData2 = createLabData(labFolder, studentData2)
//
//        val zipFile1 = createZipFile(inputTestLab1)
//        val zipFile2 = createZipFile(inputTestLab2)
//
//        // registration
//        userService.addTeacher(teacherData)
//        userService.addStudent(studentData1)
//        userService.addStudent(studentData2)
//
//        // add student access to folder
//        userService.updateStudentAccessByEmail(teacherData.id, studentData1.email, labFolder)
//        userService.updateStudentAccessByEmail(teacherData.id, studentData2.email, labFolder)
//
//        // teacher add template lab
//        userService.addTemplateByTeacher(teacherData.id, labData1.createLabFolder(), createZipFile(inputTestLab1))
//
//        // student add lab
//        userService.addLab(studentData1.id, labData1, zipFile1)
//        userService.addLab(studentData2.id, labData2, zipFile2)
//
//        // student check plag lab
//        val resultStudent = userService.checkPlagLabByStudent(studentData1.id, labData1)
//        println("--- Result by student  ====  $resultStudent")
//        assertThat(resultStudent).isNotNull
//
//        // teacher check lab
//        val resultTeacher = userService.checkPlagLabByTeacher(teacherData.id, labFolder)
//        println("--- Result by teacher  ====  $resultTeacher")
//        assertThat(resultTeacher).isNotNull
//
//        // student check test lab
//        val checkCompileLabByStudent = userService.checkCompileLabByStudent(studentData2.id, labData2)
//        val checkTestsLabByStudent = userService.checkTestsLabByStudent(studentData2.id, labData2)
//        assertThat(checkCompileLabByStudent).isEqualTo(Result.success("ok"))
//        assertThat(checkTestsLabByStudent).isEqualTo(Result.success("ok"))
//    }
//
//    @Test
//    fun `should get template lab`() {
//        // GIVEN
//        val teacherData = TeacherData.random()
//        val studentData = StudentData.random()
//
//        val labFolder = LabData.random().createLabFolder()
//        val labData = createLabData(labFolder, studentData)
//        val zipFileTemplate = createZipFile(inputTestLab1)
//
//        // registration
//        userService.addTeacher(teacherData)
//        userService.addStudent(studentData)
//
//        // add student access to folder
//        userService.updateStudentAccessByEmail(teacherData.id, studentData.email, labFolder)
//
//        // teacher add template lab
//        userService.addTemplateByTeacher(teacherData.id, labData.createLabFolder(), zipFileTemplate)
//
//        // student download template lab
//        val downloadTemplateLab = userService.downloadTemplateLab(studentData.id, labData)
//
//        assertThat(downloadTemplateLab).isNotNull
//    }
//
//    private fun createLabData(
//        labFolder: LabFolder,
//        studentData: StudentData
//    ) = LabData(
//        idTeacher = labFolder.idTeacher,
//        subject = labFolder.subject,
//        labNumber = labFolder.labNumber,
//        group = studentData.group,
//        surname = studentData.surname,
//        name = studentData.name
//    )
//
//    private fun createZipFile(fileName: String): ZipFile {
//        val archiveZip = ZipFile(zipTemplate + "zip_${randomAlphabetic(10)}.zip")
//        archiveZip.addFolder(File(fileName))
//        return archiveZip
//    }
//
//    companion object {
//        const val zipTemplate = "xFiles\\input_zip_files\\"
//        const val inputTestLab1 = "xFiles\\input_lab_files\\project_test"
//        const val inputTestLab2 = "xFiles\\input_lab_files\\project_test2"
//    }
//}
