package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class MultiAppRunArgsFunTest extends BaseFunTest implements HelloWorldProject {

    @IgnoreIf({crossCompilation || !multiAppSupported})
    def "multiAppRunArgs is passed to startup accelerator and run"() {
        when:
        def result = runGradle('jetBuild', 'jetRun')

        then:
        buildExeFile.exists()
        appExeFile.exists()

        !result.output.contains("No arguments specified")
        !result.output.contains("The application has terminated with exit code: 3")
        result.output.contains("The application has terminated with exit code: 0")
        result.output.contains("0: arg1")
        result.output.contains("1: arg2.1, arg2.2")

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
        result.task(":jetRun").outcome == TaskOutcome.SUCCESS
    }

    public String testProjectDir() {
        return "38-multiapp-run-arguments"
    }

}
