package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class MultiAppFunTest extends BaseFunTest implements HelloWorldProject {

    def "jetBuild task builds simple multiapp application"() {
        when:
        def result = runGradle('jetBuild')

        then:
        appExeFile.exists()

        checkStdOutContains("Hello World",  appExeFile)
        checkStdOutContains("Hello World2", appExeFile, "HelloWorld2")

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "06-multiapp"
    }

}
