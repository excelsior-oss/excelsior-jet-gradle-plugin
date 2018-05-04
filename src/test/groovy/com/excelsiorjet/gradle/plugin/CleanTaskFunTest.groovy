package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome
import spock.lang.IgnoreIf

class CleanTaskFunTest extends BaseFunTest implements HelloWorldProject {

    def "perform jetClean task"() {
        when:
        def pdbDir = new File(basedir, "target/jet/build/HelloWorld_jetpdb")
        def result = runGradle('jetBuild', 'jetClean')

        then:
        appExeFile.exists()
        !pdbDir.exists()

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
        result.task(":jetClean").outcome == TaskOutcome.SUCCESS
    }

    public String testProjectDir() {
        return "43-clean-task"
    }

}
