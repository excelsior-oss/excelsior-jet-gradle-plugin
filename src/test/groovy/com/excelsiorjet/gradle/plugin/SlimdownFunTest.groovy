package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class SlimdownFunTest extends BaseFunTest {

    def "test java runtime slimdown"() {
        when:
        def cleanResult = runGradle('clean')
        def testRunResult = runGradle('jetTestRun')
        def jetBuildResult = runGradle('jetBuild')

        int jetRtFilesCount = new File(basedir, "build/jet/app/rt/jetrt").listFiles().length;
        File rt0Jar = new File(basedir, "build/jet/app/rt/lib/rt-0.jar")

        then:
        (jetRtFilesCount > 1 || rt0Jar.exists())

        cleanResult.task(":clean").outcome == TaskOutcome.SUCCESS
        testRunResult.task(":jetTestRun").outcome == TaskOutcome.SUCCESS
        jetBuildResult.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "09-slimdown"
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
