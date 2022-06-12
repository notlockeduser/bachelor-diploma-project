package com.holovin.cluster.data.service

import net.lingala.zip4j.ZipFile
import org.apache.commons.lang3.RandomStringUtils
import org.kohsuke.github.GitHubBuilder
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import javax.servlet.http.HttpServletResponse

@Component
class DataService(
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
        zipFile.extractAll(rootFolder + "\\" + labFolder)
    }

    fun saveLabs(archiveLab: ZipFile, labFolder: String) {
        archiveLab.extractAll(rootFolder + "\\" + labFolder)
    }

    fun getTemplate(labFolder: String): ByteArray {
        val archiveZip = ZipFile(toUpload + "\\" + "zip_${RandomStringUtils.randomAlphabetic(10)}.zip")
        archiveZip.addFolder(File("$rootFolder\\${labFolder + "_template"}\\template"))

        return archiveZip.file.readBytes()
    }

    fun getFromGitHub(labFolder: String, labName: String, ownerReposUrl: String) {
        val token = gitHubTokenRepository.findAll().first().token

        val gitHub = GitHubBuilder()
            .withOAuthToken(token)
            .build()

        val repository = gitHub.getRepository(ownerReposUrl)

        val cd = "cd ${rootFolder + "\\" + labFolder + "\\"}"
        val command = "git clone ${repository.httpTransportUrl}"
        val ren = "ren  ${repository.name} $labName"
        val cmd = "cmd /c start cmd.exe /K \"$cd & $command & $ren & EXIT \""

        Runtime.getRuntime().exec(cmd).waitFor()
        Thread.sleep(3000)
    }

    companion object {
        private const val absolute = "C:\\Users\\Bogdan\\Desktop\\Diploma\\cluster\\"
        const val rootFolder = "xFiles\\database_lab_files"
        const val zip_files = absolute + "xFiles\\database_zip_files"
        const val toUpload = "xFiles\\database_to_upload"
    }
}
