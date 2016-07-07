package com.excelsiorjet.gradle.plugin

import org.gradle.testkit.runner.TaskOutcome

class AppWithTransDepFunTest extends BaseFunTest {

    def "jetBuild task builds simple application with transitive dependency"() {
        setup:
        File dep = new File(basedir, "build/jet/build/AppWithDep_jetpdb/tmpres/jackson-databind-2.8.0__1.jar")
        File transDep = new File(basedir, "build/jet/build/AppWithDep_jetpdb/tmpres/jackson-core-2.8.0__3.jar")

        when:
        def result = runGradle('jetBuild')

        then:
        buildExeFile.exists()
        appExeFile.exists()
        zipFile.exists()
        dep.exists()
        transDep.exists()

        checkStdOutContains(appExeFile, "field1")

        result.task(":jetBuild").outcome == TaskOutcome.SUCCESS
    }


    @Override
    protected String testProjectDir() {
        return "16-with-transitive-dependency"
    }

    @Override
    protected String projectName() {
        return "AppWithDep"
    }

    @Override
    protected String projectVersion() {
        return "1.0-SNAPSHOT"
    }
}
