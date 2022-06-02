package com.holovin.cluster.mongo

import com.holovin.cluster.xrandomizer.random
import com.holovin.cluster.user.service.domain.mongo.StudentData
import com.holovin.cluster.user.service.mongo.StudentDataRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class MongoRepositoryTest {

    @Autowired
    lateinit var studentDataRepository: StudentDataRepository

    @Test
    fun `check that student save`() {
        // GIVEN
        val studentData = StudentData.random()

        // WHEN
        studentDataRepository.save(studentData)
        val actual = studentDataRepository.findById(studentData.id).get()

        // THEN
        assertThat(actual).isEqualTo(studentData)
    }

    @Test
    fun `check that student update`() {
        // GIVEN
        val studentData = StudentData.random()
        studentDataRepository.save(studentData)
        studentData.email = "example@gmail.com"

        // WHEN
        studentDataRepository.save(studentData)
        val actual = studentDataRepository.findById(studentData.id).get()

        // THEN
        assertThat(actual).isEqualTo(studentData)
    }
}
