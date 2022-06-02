package com.holovin.cluster.test.service

import org.apache.maven.plugin.surefire.log.api.NullConsoleLogger
import org.apache.maven.plugins.surefire.report.SurefireReportParser
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.LineNumberReader
import java.util.Locale


@Component
class TestService {

    fun compileSrc(labFolder: String, labName: String) {
        val labPath = filesDb + "\\" + labFolder + "\\" + labName

        val source = "cd $labPath"
        val commandCompilationSrc = "mvn compile"
        val cmd = "cmd /c start cmd.exe /K \"$source & $commandCompilationSrc & EXIT\""

        Runtime.getRuntime().exec(cmd).waitFor()
        Thread.sleep(10000)

        checkIfCompileSuccess(labPath)
    }

    fun runTests(labFolder: String, labName: String) {
        val labPath = filesDb + "\\" + labFolder + "\\" + labName

        val source = "cd $labPath"
        val commandCompilationTest = "mvn test"
        val cmd = "cmd /c start cmd.exe /K \"$source & $commandCompilationTest & EXIT\""
        Runtime.getRuntime().exec(cmd).waitFor()
        Thread.sleep(15000)

        val surefireReportsDirectory = File("$labPath\\target\\surefire-reports")

        val parser = SurefireReportParser(listOf(surefireReportsDirectory), Locale.ENGLISH, NullConsoleLogger())
        val testSuites = parser.parseXMLReportFiles()
        for (reportTestSuite in testSuites) {
            if (reportTestSuite.numberOfErrors + reportTestSuite.numberOfFailures > 0) {
                throw IllegalArgumentException("Error test")
            }
        }
    }

    private fun checkIfCompileSuccess(labPath: String) {
        val reportCompilePath = "$labPath\\target\\maven-status\\maven-compiler-plugin\\compile\\default-compile"
        val createdFiles = "$reportCompilePath\\createdFiles.lst"
        val inputFiles = "$reportCompilePath\\inputFiles.lst"

        var lineNumberCreated = 0
        val lineNumberCreatedReader = LineNumberReader(BufferedReader(FileReader(createdFiles)))
        if (lineNumberCreatedReader.readLine() != null) lineNumberCreated = lineNumberCreatedReader.lineNumber

        var lineNumberInput = 0
        val lineNumberInputReader = LineNumberReader(BufferedReader(FileReader(inputFiles)))
        if (lineNumberInputReader.readLine() != null) lineNumberInput = lineNumberInputReader.lineNumber

        require(lineNumberCreated == lineNumberInput) { "Compile error" }
    }

    companion object {
        const val filesDb = "xFiles\\database_lab_files"
    }
}
