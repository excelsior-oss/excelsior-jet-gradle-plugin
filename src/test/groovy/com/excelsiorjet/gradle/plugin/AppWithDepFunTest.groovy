package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class AppWithDepFunTest extends BaseFunTest {

    def "jetBuild task builds simple application with dependency"() {
        when:
        def result = runGradle()

        File dep = new File(basedir, "build/jet/build/AppWithDep_jetpdb/tmpres/commons-io-1.3.2__1.jar")
        then:
        exeFile.exists()
        zipFile.exists()
        dep.exists()
        checkStdOutContains(exeFile, "HelloWorld")
        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }


    @Override
    protected String testProjectDir() {
        return "02-withdependency"
    }

    @Override
    protected String projectName() {
        return "AppWithDep"
    }
}
