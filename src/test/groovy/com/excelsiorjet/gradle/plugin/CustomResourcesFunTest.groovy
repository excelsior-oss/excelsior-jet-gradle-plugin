package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class CustomResourcesFunTest extends BaseFunTest {

    def "test custom resources"() {
        when:
        def jetTestRunResult = runGradle('jetTestRun')
        def jetBuildResult = runGradle('jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()
        zipFile.exists()

        new File(basedir, "build/jet/app/custom.file").exists()
        new File(basedir, "build/jet/app/subdir/subdir.file").exists()
        new File(basedir, "build/jet/build/custom.file").exists()
        new File(basedir, "build/jet/build/subdir/subdir.file").exists()

        jetTestRunResult.task(":jetTestRun").outcome == TaskOutcome.SUCCESS
        jetBuildResult.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "10-customresources"
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
