package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class HelloWorldFunTest extends BaseFunTest {

    def "jetBuild task builds simple application"() {
        when:
        def result = runGradle()

        then:
        buildExeFile.exists()
        appExeFile.exists()
        zipFile.exists()
        checkStdOutContains(buildExeFile, "Hello World")
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

    @Override
    protected String projectVersion() {
        return "1.0-SNAPSHOT"
    }
}
