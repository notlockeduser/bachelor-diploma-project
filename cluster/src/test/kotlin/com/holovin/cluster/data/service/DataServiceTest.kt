//package com.holovin.cluster.data.service
//
//import com.holovin.cluster.user.service.domain.LabStudent
//import com.holovin.cluster.xrandomizer.random
//import net.lingala.zip4j.ZipFile
//import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.Test
//import java.io.File
//
//internal class DataServiceTest {
//
//    private val dataService = DataService()
//
//    @Test
//    fun `extract and save file when DataService_saveLab`() {
//        val zipFile = createZipFile()
//        val labData = LabStudent.random()
//
//        dataService.saveLab(
//            zipFile,
//            labData.createNameLabFolder(),
//            labData.createNameLab()
//        )
//
//        val actualLabFolder = File(DataService.rootFolder + "\\" + labData.createNameLabFolder())
//        val actualLabFolderByStudent =
//            File(DataService.rootFolder + "\\" + labData.createNameLabFolder() + "\\" + labData.createNameLab())
//
//        assertThat(actualLabFolder.isDirectory).isTrue
//        assertThat(actualLabFolderByStudent.isDirectory).isTrue
//    }
//
//    @Test
//    fun `extract and save file when DataService_saveLabs`() {
//        val zipFile = createZipFile(true)
//        val labData = LabStudent.random()
//
//        dataService.saveLabs(
//            zipFile,
//            labData.createNameLabFolder(),
//        )
//
//        val actualLabFolder = File(DataService.rootFolder + "\\" + labData.createNameLabFolder())
//        val actualLabFolderByStudent1 =
//            File(DataService.rootFolder + "\\" + labData.createNameLabFolder() + "\\project_test" )
//        val actualLabFolderByStudent2 =
//            File(DataService.rootFolder + "\\" + labData.createNameLabFolder()  + "\\project_test2")
//
//        assertThat(actualLabFolder.isDirectory).isTrue
//        assertThat(actualLabFolderByStudent1.isDirectory).isTrue
//        assertThat(actualLabFolderByStudent2.isDirectory).isTrue
//    }
//
//    @Test
//    fun testGetDirectories() {
//        val zipFile = createZipFile(true)
//        val labData = LabStudent.random()
//
//        dataService.saveLabs(
//            zipFile,
//            labData.createNameLabFolder(),
//        )
//        val actualLabFolder = File(DataService.rootFolder + "\\" + labData.createNameLabFolder())
//        val listFiles = actualLabFolder.listFiles()
//        assertThat(listFiles).isNull()
//    }
//
//    private fun createZipFile(manyFiles: Boolean = false): ZipFile {
//        val archiveZip = ZipFile(zipTemplate + "zip_${randomAlphabetic(10)}.zip")
//        archiveZip.addFolder(File(inputTestLab))
//        if (manyFiles) archiveZip.addFolder(File(inputTestLab2))
//        return archiveZip
//    }
//
//    companion object {
//        const val zipTemplate = "xFiles\\input_zip_files\\"
//        const val inputTestLab = "xFiles\\input_lab_files\\project_test"
//        const val inputTestLab2 = "xFiles\\input_lab_files\\project_test2"
//    }
//}
