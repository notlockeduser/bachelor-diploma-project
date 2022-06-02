package com.holovin.gw.security.service

import com.holovin.gw.client.user.service.UserServiceClient
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userServiceClient: UserServiceClient,
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {

        if (userServiceClient.existsStudentFromDbByEmail(email)) {
            val studentData = userServiceClient.getStudentFromDbByEmail(email)

            val grantedAuthorities: MutableSet<GrantedAuthority> = HashSet()
            grantedAuthorities.add(SimpleGrantedAuthority("STUDENT"))

            return User(studentData.email, studentData.password, grantedAuthorities)
        }

        if (userServiceClient.existsTeacherFromDbByEmail(email)) {
            val teacherData = userServiceClient.getTeacherFromDbByEmail(email)

            val grantedAuthorities: MutableSet<GrantedAuthority> = HashSet()
            grantedAuthorities.add(SimpleGrantedAuthority("TEACHER"))

            return User(teacherData.email, teacherData.password, grantedAuthorities)
        }

        return User(null, null, null)
    }
}
