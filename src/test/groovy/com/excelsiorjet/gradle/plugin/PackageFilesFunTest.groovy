package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class PackageFilesFunTest extends BaseFunTest implements HelloWorldProject {

    def "test package files"() {
        when:
        def result = (!crossCompilation) ? runGradle('jetTestRun', 'jetBuild') : runGradle('jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()
        zipFile.exists()

        if (!crossCompilation) {
            new File(basedir, "build/jet/build/customfiles/custom.file").exists()
            new File(basedir, "build/jet/build/subdir/subdir.file").exists()
        }
        new File(basedir, "build/jet/app/customfiles/custom.file").exists()
        new File(basedir, "build/jet/app/subdir/subdir.file").exists()

        if (!crossCompilation) {
            result.task(":jetTestRun").outcome == TaskOutcome.SUCCESS
        }
        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }

    @Override
    protected String testProjectDir() {
        return "31-package-files"
    }

}
