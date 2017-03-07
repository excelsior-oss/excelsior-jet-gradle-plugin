package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class HelloWorldFunTest extends BaseFunTest implements HelloWorldProject {

    def "jetBuild task builds simple application"() {
        when:
        def zipFile = new File(basedir, "build/jet/${projectName()}"  + ".zip")
        def result = runGradle('jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()
        zipFile.exists()

        checkStdOutContains("Hello World", appExeFile)

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    public String testProjectDir() {
        return "01-helloworld"
    }

}
