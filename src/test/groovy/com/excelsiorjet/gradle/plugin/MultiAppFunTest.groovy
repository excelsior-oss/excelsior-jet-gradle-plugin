package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class MultiAppFunTest extends BaseFunTest implements HelloWorldProject {

    def "jetBuild task builds simple multiapp application"() {
        when:
        def result = runGradle('jetBuild')

        then:
        appExeFile.exists()

        (appExeFile.absolutePath.execute().text.trim().equals("Hello World"))
        ((appExeFile.absolutePath + " HelloWorld2").execute().text.trim().equals("Hello World2"))

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "06-multiapp"
    }

}
