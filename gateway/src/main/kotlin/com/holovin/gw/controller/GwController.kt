package com.holovin.gw.controller

import com.holovin.gw.client.user.service.UserServiceClient
import com.holovin.gw.domain.dto.GitHubUrl
import com.holovin.gw.domain.dto.LabDescription
import com.holovin.gw.domain.dto.StudentData
import com.holovin.gw.domain.dto.TeacherData
import com.holovin.gw.domain.dto.UpdateAccessByEmail
import com.holovin.gw.domain.dto.UpdateAccessByGroup
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.view.RedirectView
import java.security.Principal

@Controller
@RequestMapping
class GwController(
    private val userServiceClient: UserServiceClient,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {

    // registration
    @GetMapping("/registration")
    fun getRegistration(): String {
        return "registration"
    }

    @GetMapping("/add_student")
    fun getAddStudent(model: Model): String {
        model.addAttribute("student", StudentData())
        return "add_student"
    }

    @PostMapping("/add_student")
    fun postAddStudent(@ModelAttribute studentData: StudentData): String {
        studentData.password = bCryptPasswordEncoder.encode(studentData.password)
        userServiceClient.addStudent(studentData)
        return "redirect:/login"
    }

    @GetMapping("/add_teacher")
    fun getAddTeacher(model: Model): String {
        model.addAttribute("teacher", TeacherData())
        return "add_teacher"
    }

    @PostMapping("/add_teacher")
    fun postAddTeacher(@ModelAttribute teacherData: TeacherData): String {
        teacherData.password = bCryptPasswordEncoder.encode(teacherData.password)
        userServiceClient.addTeacher(teacherData)
        return "redirect:/login"
    }

    // profile
    @GetMapping("/profile")
    fun getProfileTeacher(principal: Principal, model: Model): String {

        if (userServiceClient.existsStudentFromDbByEmail(principal.name)) {
            model.addAttribute("student", userServiceClient.getStudentFromDbByEmail(principal.name))
            model.addAttribute("labs", userServiceClient.findLabsByStudentEmail(principal.name))
            return "profile_student"
        }
        if (userServiceClient.existsTeacherFromDbByEmail(principal.name)) {
            model.addAttribute("teacher", userServiceClient.getTeacherFromDbByEmail(principal.name))
            model.addAttribute("labs", userServiceClient.findLabsByTeacherEmail(principal.name))
            return "profile_teacher"
        }

        return ResponseEntity.badRequest().toString()
    }

    //////// lab

    @GetMapping("/create_lab_by_teacher")
    fun getCreateLab(model: Model): String {
        model.addAttribute("lab_settings", LabDescription())
        return "create_lab_by_teacher"
    }

    @PostMapping("/create_lab")
    fun postCreateLab(principal: Principal, @ModelAttribute labDescription: LabDescription, model: Model): String {
        userServiceClient.addLabTaskByTeacher(principal.name, labDescription)
        return "redirect:/profile"
    }

    @GetMapping("/get_lab_by_teacher/{email}/{subject}/{labNumber}")
    fun getLabByTeacher(
        principal: Principal,
        @PathVariable("email") email: String,
        @PathVariable("subject") subject: String,
        @PathVariable("labNumber") labNumber: String,
        model: Model
    ): String {
        val labInfo = userServiceClient.findLabByTeacher(email, subject, labNumber)
        model.addAttribute("lab", labInfo)
        model.addAttribute("updateAccessByEmail", UpdateAccessByEmail())
        model.addAttribute("updateAccessByGroup", UpdateAccessByGroup())
        return "get_lab_by_teacher"
    }

    @GetMapping("/get_lab_by_student/{email}/{subject}/{labNumber}")
    fun getLabByStudent(
        principal: Principal,
        @PathVariable("email") teacherEmail: String,
        @PathVariable("subject") subject: String,
        @PathVariable("labNumber") labNumber: String,
        model: Model
    ): String {
        model.addAttribute(
            "lab",
            userServiceClient.findLabByStudent(principal.name, teacherEmail, subject, labNumber)
        )
        model.addAttribute("gitHubUrl", GitHubUrl())

        return "get_lab_by_student"
    }

    @GetMapping("/check_plag_lab_by_student/{email}/{subject}/{labNumber}")
    fun checkPlagLabByStudent(
        principal: Principal,
        @PathVariable("email") teacherEmail: String,
        @PathVariable("subject") subject: String,
        @PathVariable("labNumber") labNumber: String,
        model: Model
    ): String {
        userServiceClient.checkPlagByStudent(teacherEmail, principal.name, subject, labNumber)

        return "redirect:/get_lab_by_student/${teacherEmail}/${subject}/${labNumber}"
    }

    @GetMapping("/compile_lab_by_student/{email}/{subject}/{labNumber}")
    fun compileLabByStudent(
        principal: Principal,
        @PathVariable("email") teacherEmail: String,
        @PathVariable("subject") subject: String,
        @PathVariable("labNumber") labNumber: String,
        model: Model
    ): String {
        userServiceClient.compileLabByStudent(teacherEmail, principal.name, subject, labNumber)

        return "redirect:/get_lab_by_student/${teacherEmail}/${subject}/${labNumber}"
    }

    @GetMapping("/test_lab_by_student/{email}/{subject}/{labNumber}")
    fun testLabByStudent(
        principal: Principal,
        @PathVariable("email") teacherEmail: String,
        @PathVariable("subject") subject: String,
        @PathVariable("labNumber") labNumber: String,
        model: Model
    ): String {
        userServiceClient.testLabByStudent(teacherEmail, principal.name, subject, labNumber)

        return "redirect:/get_lab_by_student/${teacherEmail}/${subject}/${labNumber}"
    }

    @ResponseBody
    @PostMapping(
        "/upload_lab_by_student/{email}/{subject}/{labNumber}",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadLabByStudent(
        principal: Principal,
        @RequestParam file: MultipartFile,
        @PathVariable("email") teacherEmail: String,
        @PathVariable("subject") subject: String,
        @PathVariable("labNumber") labNumber: String,
    ): RedirectView {
        userServiceClient.addLabByStudent(teacherEmail, principal.name, subject, labNumber, file)
        return RedirectView("/get_lab_by_student/${teacherEmail}/${subject}/${labNumber}")
    }

    @GetMapping("/upload_lab_by_github/{email}/{subject}/{labNumber}")
    fun uploadLabByGithub(
        principal: Principal,
        @PathVariable("email") email: String,
        @PathVariable("subject") subject: String,
        @PathVariable("labNumber") labNumber: String,
        @ModelAttribute updateAccessByEmail: GitHubUrl, model: Model
    ): RedirectView {
        userServiceClient.uploadLabByGithub(
            email,
            principal.name,
            subject,
            labNumber,
            updateAccessByEmail.ownerRepos!!
        )

        return RedirectView("/get_lab_by_student/${email}/${subject}/${labNumber}")
    }


    @GetMapping("/update_access_by_email/{email}/{subject}/{labNumber}")
    fun updateAccessByEmailStudent(
        principal: Principal,
        @PathVariable("email") email: String,
        @PathVariable("subject") subject: String,
        @PathVariable("labNumber") labNumber: String,
        @ModelAttribute updateAccessByEmail: UpdateAccessByEmail, model: Model
    ): String {
        userServiceClient.updateStudentAccessByEmail(
            principal.name,
            updateAccessByEmail.email!!,
            subject,
            labNumber
        )

        return "forward:/get_lab_by_teacher/${principal.name}/${subject}/${labNumber}"
    }

    @GetMapping("/update_access_by_group/{email}/{subject}/{labNumber}")
    fun updateAccessByGroupStudent(
        principal: Principal,
        @PathVariable("email") email: String,
        @PathVariable("subject") subject: String,
        @PathVariable("labNumber") labNumber: String,
        @ModelAttribute updateAccessByGroup: UpdateAccessByGroup, model: Model
    ): String {
        userServiceClient.updateStudentAccessByGroup(
            principal.name,
            updateAccessByGroup.group!!,
            subject,
            labNumber
        )

        return "forward:/get_lab_by_teacher/${principal.name}/${subject}/${labNumber}"
    }

    @GetMapping("/check_plag_lab_by_teacher/{email}/{subject}/{labNumber}")
    fun checkPlagLabByTeacher(
        principal: Principal,
        @PathVariable("email") teacherEmail: String,
        @PathVariable("subject") subject: String,
        @PathVariable("labNumber") labNumber: String,
        model: Model
    ): String {
        userServiceClient.checkPlagByTeacher(teacherEmail, subject, labNumber)

        return "redirect:/get_lab_by_teacher/${teacherEmail}/${subject}/${labNumber}"
    }

    @GetMapping("/compile_lab_by_teacher/{email}/{subject}/{labNumber}")
    fun compileLabByTeacher(
        principal: Principal,
        @PathVariable("email") teacherEmail: String,
        @PathVariable("subject") subject: String,
        @PathVariable("labNumber") labNumber: String,
        model: Model
    ): String {
        userServiceClient.compileLabByTeacher(teacherEmail, subject, labNumber)

        return "redirect:/get_lab_by_teacher/${teacherEmail}/${subject}/${labNumber}"
    }

    @GetMapping("/test_lab_by_teacher/{email}/{subject}/{labNumber}")
    fun testLabByTeacher(
        principal: Principal,
        @PathVariable("email") teacherEmail: String,
        @PathVariable("subject") subject: String,
        @PathVariable("labNumber") labNumber: String,
        model: Model
    ): String {
        userServiceClient.testLabByTeacher(teacherEmail, subject, labNumber)

        return "redirect:/get_lab_by_teacher/${teacherEmail}/${subject}/${labNumber}"
    }
}