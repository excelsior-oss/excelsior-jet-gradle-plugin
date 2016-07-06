package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class HelloWorldFunTest extends BaseFunTest implements HelloWorldProject {

    def "jetBuild task builds simple application"() {
        when:
        def result = runGradle('jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()
        zipFile.exists()

        checkStdOutContains(appExeFile, "Hello World")

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    public String testProjectDir() {
        return "01-helloworld"
    }

}
