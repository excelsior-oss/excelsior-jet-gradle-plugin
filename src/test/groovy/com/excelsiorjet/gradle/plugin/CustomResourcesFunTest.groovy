package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class CustomResourcesFunTest extends BaseFunTest implements HelloWorldProject {

    def "test custom resources"() {
        when:
        def result = runGradle('jetTestRun', 'jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()
        zipFile.exists()

        new File(basedir, "build/jet/app/custom.file").exists()
        new File(basedir, "build/jet/app/subdir/subdir.file").exists()
        new File(basedir, "build/jet/build/custom.file").exists()
        new File(basedir, "build/jet/build/subdir/subdir.file").exists()

        result.task(":jetTestRun").outcome == TaskOutcome.SUCCESS
        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "10-customresources"
    }

}
