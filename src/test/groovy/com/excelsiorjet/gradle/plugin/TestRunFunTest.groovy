package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class TestRunFunTest extends BaseFunTest {

    def "executes test run for project"() {
        when:
        def testRunResult = runGradle('jetTestRun')
        def jetBuildResult = runGradle('jetBuild')

        File startupProfile = new File( basedir, "src/main/jetresources/HelloWorld.startup");
        File reorderFile = new File(basedir, "build/jet/build/HelloWorld_jetpdb/reorder.li")

        then:
        startupProfile.exists()
        reorderFile.exists();

        testRunResult.task(":jetTestRun").outcome == TaskOutcome.SUCCESS
        jetBuildResult.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "07-testrun"
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
