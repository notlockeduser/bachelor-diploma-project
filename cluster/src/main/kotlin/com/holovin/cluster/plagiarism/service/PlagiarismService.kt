package com.holovin.cluster.plagiarism.service

import de.jplag.JPlag
import de.jplag.JPlagResult
import de.jplag.options.JPlagOptions
import de.jplag.options.LanguageOption
import de.jplag.reporting.Report
import org.springframework.stereotype.Component
import java.io.File

@Component
class PlagiarismService {

    fun checkLabByStudent(labFolder: String, studentLabName: String): Float {
        val options = createJPlagOptions(labFolder)
        val result = JPlag(options).run()

//        webOutputResult(options, result)

        return result.comparisons
            .filter { it.firstSubmission.name == studentLabName || it.secondSubmission.name == studentLabName }
            .maxOf { it.similarity() }
    }

    fun checkLabByTeacher(labFolder: String): List<Pair<String, Float>> {

        val options = createJPlagOptions(labFolder)
        val result = JPlag(options).run()

        val actualLabFolder = File(filesDb + "\\" + labFolder)
        val listNameLabs = actualLabFolder.listFiles()!!

        return listNameLabs.map { it.name }
            .map { studentLabName ->
                studentLabName to result.comparisons
                    .filter { it.firstSubmission.name == studentLabName || it.secondSubmission.name == studentLabName }
                    .maxOf { it.similarity() }
            }
    }

    private fun webOutputResult(options: JPlagOptions, result: JPlagResult?) {
        val outputDir = File(webOutput)
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
