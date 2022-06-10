package com.holovin.cluster.data.service

import net.lingala.zip4j.ZipFile
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.junit.jupiter.api.Test
import org.kohsuke.github.GitHubBuilder
import java.io.File

internal class DataServiceTest {

    private val dataService = DataService()

    @Test
    fun `test gitHub`() {
        dataService.getFromGitHub(
            "id-939_sub601_lab2",
            "id-939_sub601_lab2_github",
            "jeniknaum5/FirstProjectUNO-console-"
        )
    }

    private fun createZipFile(manyFiles: Boolean = false): ZipFile {
        val archiveZip = ZipFile(zipTemplate + "zip_${randomAlphabetic(10)}.zip")
        archiveZip.addFolder(File(inputTestLab))
        if (manyFiles) archiveZip.addFolder(File(inputTestLab2))
        return archiveZip
    }

    companion object {
        const val zipTemplate = "xFiles\\input_zip_files\\"
        const val inputTestLab = "xFiles\\input_lab_files\\project_test"
        const val inputTestLab2 = "xFiles\\input_lab_files\\project_test2"
    }
}
