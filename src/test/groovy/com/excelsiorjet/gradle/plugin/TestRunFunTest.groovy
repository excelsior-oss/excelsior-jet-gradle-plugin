package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class TestRunFunTest extends BaseFunTest implements HelloWorldProject {

    def "executes test run for project"() {
        setup:
        File startupProfile = new File( basedir, "src/main/jetresources/HelloWorld.startup");
        File reorderFile = new File(basedir, "build/jet/build/HelloWorld_jetpdb/reorder.li")

        when:
        def result = runGradle('jetTestRun', 'jetBuild')

        then:
        startupProfile.exists()
        reorderFile.exists();

        result.task(":jetTestRun").outcome == TaskOutcome.SUCCESS
        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "07-testrun"
    }

}
