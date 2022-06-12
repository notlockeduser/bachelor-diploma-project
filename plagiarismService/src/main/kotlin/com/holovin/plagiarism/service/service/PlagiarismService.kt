package com.holovin.plagiarism.service.service

import com.holovin.plagiarism.service.mongo.PlagResultData
import com.holovin.plagiarism.service.mongo.PlagResultDataRepository
import de.jplag.JPlag
import de.jplag.JPlagResult
import de.jplag.options.JPlagOptions
import de.jplag.options.LanguageOption
import de.jplag.reporting.Report
import net.lingala.zip4j.ZipFile
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Component
import java.io.File

@Component
class PlagiarismService(val repository: PlagResultDataRepository) {

    fun checkLabByStudent(labFolder: String, studentLabName: String): Float {
        val options = createJPlagOptions(labFolder)
        val result = JPlag(options).run()

        webOutputResult(options, result, labFolder, studentLabName)

        val maxOf = result.comparisons
            .filter { it.firstSubmission.name == studentLabName || it.secondSubmission.name == studentLabName }
            .maxOf { it.similarity() }

        val dataOptional = repository.findByLabFolderAndLabName(labFolder, studentLabName)

        if (dataOptional.isPresent) {
            val compileData = dataOptional.get()
            compileData.result = maxOf.toString()
            repository.save(compileData)
        } else {
            repository.save(
                PlagResultData(
                    labFolder = labFolder,
                    labName = studentLabName,
                    result = maxOf.toString()
                )
            )
        }

        return maxOf
    }

    fun getResultPlag(labFolder: String, labName: String): String {
        val resultDataOptional = repository.findByLabFolderAndLabName(labFolder, labName)
        return when {
            resultDataOptional.isPresent -> resultDataOptional.get().result
            else -> "Internal error"
        }
    }

    fun getResultZipWeb(labFolder: String, labName: String): ByteArray {

        val archiveZip = ZipFile(webOutputZip + "\\" + "zip_${RandomStringUtils.randomAlphabetic(10)}.zip")
        archiveZip.addFolder(File("$webOutput\\$labFolder\\$labName"))

        return archiveZip.file.readBytes()
    }

    private fun webOutputResult(
        options: JPlagOptions,
        result: JPlagResult?,
        labFolder: String,
        studentLabName: String
    ) {
        val outputDir = File(webOutput + "\\" + labFolder + "\\" + studentLabName)
        val report = Report(outputDir, options)
        report.writeResult(result)
    }

    private fun createJPlagOptions(labName: String = ""): JPlagOptions {
        val options = JPlagOptions(
            filesDb + "\\" + labName,
            LanguageOption.JAVA
        )
        options.minimumTokenMatch = 1
        return options
    }

    companion object {
        private const val absolute = "C:\\Users\\Bogdan\\Desktop\\Diploma\\plagiarismService\\"
        private const val absoluteDataService = "C:\\Users\\Bogdan\\Desktop\\Diploma\\dataService\\"
        const val filesDb = absoluteDataService + "xFiles\\database_lab_files"
        const val webOutput = absolute + "xFiles\\web_output"
        const val webOutputZip = absolute + "xFiles\\web_output_zip"
    }
}
