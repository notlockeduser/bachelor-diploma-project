package com.holovin.data.service.service

import com.holovin.data.service.mongo.GitHubTokenRepository
import net.lingala.zip4j.ZipFile
import org.apache.commons.lang3.RandomStringUtils
import org.kohsuke.github.GitHubBuilder
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Component
class Service(
    private val gitHubTokenRepository: GitHubTokenRepository
) {

    fun saveLab(archiveLab: MultipartFile, labFolder: String, labName: String) {
        // save file as .zip
        val file = File(zip_files + "\\zip_${RandomStringUtils.randomAlphabetic(10)}.zip")
        file.createNewFile()
        archiveLab.transferTo(file)

        // extract zip file
        val zipFile = ZipFile(file)
        zipFile.renameFile(zipFile.fileHeaders.last(), labName)
        zipFile.extractAll(labFiles + "\\" + labFolder)
    }

    fun saveLabs(archiveLabs: MultipartFile, labFolder: String) {
        // save file as .zip
        val file = File(zip_files + "\\zip_${RandomStringUtils.randomAlphabetic(10)}.zip")
        file.createNewFile()
        archiveLabs.transferTo(file)

        // extract zip file
        val zipFile = ZipFile(file)
        zipFile.extractAll(labFiles + "\\" + labFolder)
    }

    fun getTemplate(labFolder: String): ByteArray {
        val archiveZip = ZipFile(toUpload + "\\" + "zip_${RandomStringUtils.randomAlphabetic(10)}.zip")
        archiveZip.addFolder(File("$labFiles\\${labFolder + "_template"}\\template"))

        return archiveZip.file.readBytes()
    }

    fun getFromGitHub(labFolder: String, labName: String, ownerReposUrl: String) {
        val token = gitHubTokenRepository.findAll().first().token

        val gitHub = GitHubBuilder()
            .withOAuthToken(token)
            .build()

        val repository = gitHub.getRepository(ownerReposUrl)

        if (!(File(labFiles + "\\" + labFolder + "\\").isDirectory)){
            val cd = "cd ${labFiles + "\\"}"
            val md = "md $labFolder"
            val cmd = "cmd /c start cmd.exe /K \"$cd & $md & EXIT \""
            Runtime.getRuntime().exec(cmd).waitFor()
            Thread.sleep(100)
        }

        val cd2 = "cd ${labFiles + "\\" + labFolder + "\\"}"
        val command = "git clone ${repository.httpTransportUrl}"
        val ren = "ren  ${repository.name} $labName"
        val cmd = "cmd /c start cmd.exe /K \"$cd2 & $command & $ren & EXIT \""

        Runtime.getRuntime().exec(cmd).waitFor()
        Thread.sleep(3000)
    }

    companion object {
        private const val absolute = "C:\\Users\\Bogdan\\Desktop\\Diploma\\dataService\\"
        const val labFiles = absolute + "xFiles\\database_lab_files"
        const val zip_files = absolute + "xFiles\\database_zip_files"
        const val toUpload = absolute + "xFiles\\database_to_upload"
    }
}
