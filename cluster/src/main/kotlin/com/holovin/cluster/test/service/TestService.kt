package com.holovin.cluster.test.service

import com.holovin.cluster.test.service.domain.CompileAndTestData
import com.holovin.cluster.test.service.domain.CompileAndTestDataRepository
import org.apache.maven.plugin.surefire.log.api.NullConsoleLogger
import org.apache.maven.plugins.surefire.report.SurefireReportParser
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.LineNumberReader
import java.util.Locale


@Component
class TestService(
    val compileDataRepository: CompileAndTestDataRepository
) {

    fun compileSrc(labFolder: String, labName: String) {
        val labPath = filesDb + "\\" + labFolder + "\\" + labName

        Thread {
            val source = "cd $labPath"
            val commandCompilationSrc = "mvn compile"
            val cmd = "cmd /c start cmd.exe /K \"$source & $commandCompilationSrc & EXIT\""

            Runtime.getRuntime().exec(cmd).waitFor()
            Thread.sleep(10000)

            checkIfCompileSuccess(labFolder, labName, labPath)
        }.start()

    }

    fun getResultCompile(labFolder: String, labName: String): String {
        val compileDataOptional = compileDataRepository.findByLabFolderAndLabName(labFolder, labName)
        return when {
            compileDataOptional.isPresent -> compileDataOptional.get().compileResult
            else -> "Internal error"
        }
    }

    fun getResultTest(labFolder: String, labName: String): String {
        val compileDataOptional = compileDataRepository.findByLabFolderAndLabName(labFolder, labName)
        return when {
            compileDataOptional.isPresent -> compileDataOptional.get().testResult
            else -> "Internal error"
        }
    }

    fun runTests(labFolder: String, labName: String) {
        val labPath = filesDb + "\\" + labFolder + "\\" + labName

        Thread {
            val source = "cd $labPath"
            val commandCompilationTest = "mvn test"
            val cmd = "cmd /c start cmd.exe /K \"$source & $commandCompilationTest & EXIT\""
            Runtime.getRuntime().exec(cmd).waitFor()
            Thread.sleep(15000)

            val surefireReportsDirectory = File("$labPath\\target\\surefire-reports")

            val parser = SurefireReportParser(listOf(surefireReportsDirectory), Locale.ENGLISH, NullConsoleLogger())
            val testSuites = parser.parseXMLReportFiles()

            var errorsCount = 0
            for (reportTestSuite in testSuites) {
                if (reportTestSuite.numberOfErrors + reportTestSuite.numberOfFailures > 0) {
                    errorsCount += (reportTestSuite.numberOfErrors + reportTestSuite.numberOfFailures)
                }
            }

            when (errorsCount) {
                0 -> {
                    val compileDataOptional = compileDataRepository.findByLabFolderAndLabName(labFolder, labName)

                    if (compileDataOptional.isPresent) {
                        val compileData = compileDataOptional.get()
                        compileData.testResult = "OK"
                        compileDataRepository.save(compileData)
                    } else {
                        compileDataRepository.save(
                            CompileAndTestData(
                                labFolder = labFolder,
                                labName = labName,
                                compileResult = "Not tested",
                                testResult = "Ok"
                            )
                        )
                    }
                }
                else -> {
                    val compileDataOptional = compileDataRepository.findByLabFolderAndLabName(labFolder, labName)

                    if (compileDataOptional.isPresent) {
                        val compileData = compileDataOptional.get()
                        compileData.testResult = "Code have problems - $errorsCount count"
                        compileDataRepository.save(compileData)
                    } else {
                        compileDataRepository.save(
                            CompileAndTestData(
                                labFolder = labFolder,
                                labName = labName,
                                compileResult = "Not tested",
                                testResult = "Code have problems - $errorsCount count"
                            )
                        )
                    }
                }
            }
        }.start()
    }

    private fun checkIfCompileSuccess(labFolder: String, labName: String, labPath: String) {
        try {
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

            val compileDataOptional = compileDataRepository.findByLabFolderAndLabName(labFolder, labName)

            if (compileDataOptional.isPresent) {
                val compileData = compileDataOptional.get()
                compileData.compileResult = "OK"
                compileDataRepository.save(compileData)
            } else {
                compileDataRepository.save(
                    CompileAndTestData(
                        labFolder = labFolder,
                        labName = labName,
                        compileResult = "OK",
                        testResult = "Not tested"
                    )
                )
            }

        } catch (e: Exception) {
            val compileDataOptional = compileDataRepository.findByLabFolderAndLabName(labFolder, labName)

            if (compileDataOptional.isPresent) {
                val compileData = compileDataOptional.get()
                compileData.compileResult = "Failed"
                compileDataRepository.save(compileData)
            } else {
                compileDataRepository.save(
                    CompileAndTestData(
                        labFolder = labFolder,
                        labName = labName,
                        compileResult = "Failed",
                        testResult = "Not tested"
                    )
                )
            }

        }
    }

    companion object {
        private const val absoluteDataService = "C:\\Users\\Bogdan\\Desktop\\Diploma\\dataService\\"
        const val filesDb = absoluteDataService + "xFiles\\database_lab_files"
    }
}
