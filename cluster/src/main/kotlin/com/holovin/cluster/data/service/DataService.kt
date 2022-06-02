package com.holovin.cluster.data.service

import net.lingala.zip4j.ZipFile
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Component
class DataService {

    fun saveLab(archiveLab: MultipartFile, labFolder: String, labName: String) {
        // save file as .zip
        val file = File(zip_files + "\\zip_${RandomStringUtils.randomAlphabetic(10)}.zip")
        archiveLab.transferTo(file)

        // extract zip file
        val zipFile = ZipFile(file)
        zipFile.renameFile(zipFile.fileHeaders.last(), labName)
        zipFile.extractAll(rootFolder + "\\" + labFolder)
    }

    fun saveLabs(archiveLab: ZipFile, labFolder: String) {
        archiveLab.extractAll(rootFolder + "\\" + labFolder)
    }

    fun getTemplate(labFolder: String): ZipFile {
        val archiveZip = ZipFile(toUpload + "\\" + "zip_${RandomStringUtils.randomAlphabetic(10)}.zip")
        archiveZip.addFolder(File("$rootFolder\\$labFolder\\template"));
        return archiveZip
    }

    companion object {
        const val rootFolder = "xFiles\\database_lab_files"
        const val zip_files = "xFiles\\database_zip_files"
        const val toUpload = "xFiles\\database_to_upload"
    }
}
