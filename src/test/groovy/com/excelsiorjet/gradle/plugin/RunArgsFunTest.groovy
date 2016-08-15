package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class RunArgsFunTest extends BaseFunTest implements HelloWorldProject {

    def "runArgs is passed to startup accelerator and test runs"() {
        when:
        def result = runGradle('jetTestRun', 'jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()
        zipFile.exists()

        !result.output.contains("No arguments specified")
        !result.output.contains("The application has terminated with exit code: 3")
        result.output.contains("The application has terminated with exit code: 0")
        result.output.contains("0: arg1")
        result.output.contains("1: arg2.1, arg2.2")

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
        result.task(":jetTestRun").outcome == TaskOutcome.SUCCESS
    }

    public String testProjectDir() {
        return "19-run-arguments"
    }

}
