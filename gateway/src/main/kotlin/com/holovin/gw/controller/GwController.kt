package com.holovin.gw.controller

import com.holovin.gw.client.user.service.UserServiceClient
import com.holovin.gw.domain.dto.LabDescription
import com.holovin.gw.domain.dto.StudentData
import com.holovin.gw.domain.dto.TeacherData
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
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
        model.addAttribute("lab", userServiceClient.findLabByTeacher(email, subject, labNumber))
        return "get_lab_by_teacher"
    }

////
////    @PostMapping("/add_teacher")
////    fun addTeacher(
////        @RequestParam name: String,
////        @RequestParam surname: String,
////        @RequestParam password: String,
////        @RequestParam email: String
////    ): ResponseEntity<String> {
////        val teacherData = TeacherData(name, surname, password, email)
////        val addTeacherResponse = userService.addTeacher(teacherData)
////        return ResponseEntity.ok("Teacher add = $addTeacherResponse")
////    }
//
//    @PostMapping("/upload_by_student")
//    fun uploadZipLabByStudent(
//        @RequestParam file: MultipartFile,
//        @RequestParam studentId: String,
//        @RequestParam labData: LabData
//    ): ResponseEntity<String> {
//        try {
//            if (file.contentType != "application/zip") return ResponseEntity.badRequest()
//                .body("Type of upload files need = application/zip, current = " + file.contentType)
//
//            userService.addLab(studentId, labData, file)
//
//
//            return ResponseEntity.ok("Upload file successfully: " + file.originalFilename)
//        } catch (e: Exception) {
//            return ResponseEntity.badRequest().body("Could not upload the file:" + file.originalFilename)
//        }
//    }
}