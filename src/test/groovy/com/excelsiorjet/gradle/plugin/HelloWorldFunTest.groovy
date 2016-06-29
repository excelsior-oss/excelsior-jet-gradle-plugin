package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class HelloWorldFunTest extends BaseFunTest {

    def "jetBuild task builds simple application"() {
        when:
        def result = runGradle()

        then:
        exeFile.exists()
        zipFile.exists()
        checkStdOutContains(exeFile, "Hello World")
        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }


    @Override
    protected String testProjectDir() {
        return "01-helloworld"
    }

    @Override
    protected String projectName() {
        return "HelloWorld"
    }
}
