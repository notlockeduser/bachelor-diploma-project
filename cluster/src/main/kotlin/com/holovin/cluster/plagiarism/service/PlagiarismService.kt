package com.holovin.cluster.plagiarism.service

import de.jplag.JPlag
import de.jplag.JPlagResult
import de.jplag.options.JPlagOptions
import de.jplag.options.LanguageOption
import de.jplag.reporting.Report
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

    fun getResultZipWeb(labFolder: String, labName: String): String {
        val resultDataOptional = repository.findByLabFolderAndLabName(labFolder, labName)
        return when {
            resultDataOptional.isPresent -> resultDataOptional.get().result
            else -> "Internal error"
        }
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
        options.exclusionFileName = "template"
        options.minimumTokenMatch = 1
        return options
    }

    companion object {
        const val filesDb = "xFiles\\database_lab_files"
        const val webOutput = "xFiles\\web_output"

        // options.comparisonMode = ComparisonMode.NORMAL
        // options.baseCodeSubmissionName = "template"
    }
}
